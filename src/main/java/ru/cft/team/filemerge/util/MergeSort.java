package ru.cft.team.filemerge.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.cft.team.filemerge.exception.FileDeleteException;
import ru.cft.team.filemerge.exception.SortingException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

import static ru.cft.team.filemerge.util.Constant.*;

@UtilityClass
@Slf4j
public class MergeSort {
    public <T extends Comparable<? super T>> void sort(List<String> filePaths, String sortMode,
                                                       Function<String, ? extends T> parser, String outputFilePath) {
        int chunkSize = 1000;

        try {
            List<String> chunkFilePaths = new ArrayList<>();

            for (int i = 0; i < filePaths.size(); i += chunkSize) {
                List<String> chunk = filePaths.subList(i, Math.min(i + chunkSize, filePaths.size()));

                List<T> data = readAndParseChunks(chunk, parser);
                Collections.sort(data);

                String chunkFilePath = generateTempFilePath(i);
                chunkFilePaths.add(chunkFilePath);
                writeToFile(data, chunkFilePath);
            }

            List<T> sortedData = mergeChunks(chunkFilePaths, sortMode, parser);
            writeToFile(sortedData, outputFilePath);

            chunkFilePaths.forEach(chunkFilePath -> {
                try {
                    Files.delete(Paths.get(chunkFilePath));
                } catch (IOException e) {
                    throw new FileDeleteException("File deletion error. " + e);
                }
            });

        } catch (IOException e) {
            log.error("Error while sorting files.");
            throw new SortingException("Error while sorting files. " + e);
        }
    }

    private String generateTempFilePath(int index) {
        return TEMP_DIRECTORY + "temp_chunk_" + index + ".txt";
    }

    private <T> List<T> readAndParseChunks(List<String> filePaths, Function<String, ? extends T> parser) {
        Objects.requireNonNull(filePaths, "filePaths cannot null");
        Objects.requireNonNull(parser, "parser cannot be null");

        List<T> data = new ArrayList<>();
        filePaths.forEach(filePath -> data.addAll(CustomFileReader.read(filePath, parser)));
        return data;
    }

    private <T> void writeToFile(List<T> data, String filePath) {
        CustomFileWriter.write(filePath, data);
    }

    private <T extends Comparable<? super T>> List<T> mergeChunks(List<String> chunkFilePaths, String sortMode,
                                                                  Function<String, ? extends T> parser) throws IOException {
        List<IndexedBufferedReader<T>> readers = new ArrayList<>();

        for (int i = 0; i < chunkFilePaths.size(); i++) {
            String chunkFilePath = chunkFilePaths.get(i);
            BufferedReader reader = Files.newBufferedReader(Paths.get(chunkFilePath));
            String line = reader.readLine();
            if (line != null) {
                T item = parser.apply(line);
                readers.add(new IndexedBufferedReader<>(reader, item, i));
            } else {
                reader.close();
            }
        }

        List<T> sortedData = new ArrayList<>();
        while (!readers.isEmpty()) {
            int minIndex = findMinIndex(readers, sortMode);

            if (minIndex != -1) {
                IndexedBufferedReader<T> minReader = readers.get(minIndex);
                sortedData.add(minReader.currentItem);
                String nextLine = minReader.bufferedReader.readLine();
                if (nextLine != null) {
                    minReader.currentItem = parser.apply(nextLine);
                } else {
                    minReader.currentItem = null;
                    minReader.bufferedReader.close();
                    readers.remove(minIndex);
                }
            } else {
                break;
            }
        }

        if (sortMode.equals(SORT_MODE_DESCENDING)) {
            Collections.reverse(sortedData);
        }

        return sortedData;
    }

    private <T extends Comparable<? super T>> int findMinIndex(List<IndexedBufferedReader<T>> readers, String sortMode) {
        return IntStream.range(0, readers.size())
                .filter(i -> readers.get(i).currentItem != null)
                .boxed()
                .min((i1, i2) -> {
                    IndexedBufferedReader<T> reader1 = readers.get(i1);
                    IndexedBufferedReader<T> reader2 = readers.get(i2);
                    T currentItem1 = reader1.currentItem;
                    T currentItem2 = reader2.currentItem;

                    int compareResult;
                    if (sortMode.equals(SORT_MODE_ASCENDING)) {
                        compareResult = currentItem1.compareTo(currentItem2);
                    } else {
                        compareResult = currentItem2.compareTo(currentItem1);
                    }

                    return compareResult;
                })
                .orElse(-1);
    }

}

class IndexedBufferedReader<T> {
    BufferedReader bufferedReader;
    T currentItem;
    int index;

    IndexedBufferedReader(BufferedReader bufferedReader, T currentItem, int index) {
        this.bufferedReader = bufferedReader;
        this.currentItem = currentItem;
        this.index = index;
    }
}
