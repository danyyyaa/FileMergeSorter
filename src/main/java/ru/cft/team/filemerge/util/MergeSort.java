package ru.cft.team.filemerge.util;

import lombok.experimental.UtilityClass;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Function;

import static ru.cft.team.filemerge.util.Constant.*;

@UtilityClass
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
            System.out.println(sortedData);

            for (String chunkFilePath : chunkFilePaths) {
                Files.delete(Path.of(chunkFilePath));
            }
        } catch (IOException e) {
            throw new RuntimeException("Ошибка при сортировке файлов.", e);
        }
    }

    private String generateTempFilePath(int index) {
        return TEMP_DIRECTORY + "temp_chunk_" + index + ".txt";
    }

    private <T> List<T> readAndParseChunks(List<String> filePaths, Function<String, ? extends T> parser) throws IOException {
        List<T> data = new ArrayList<>();
        for (String filePath : filePaths) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    T item = parser.apply(line);
                    data.add(item);
                }
            }
        }
        return data;
    }

    private <T> void writeToFile(List<T> data, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            for (T item : data) {
                writer.write(item.toString());
                writer.newLine();
            }
        }
    }

    private <T extends Comparable<? super T>> List<T> mergeChunks(List<String> chunkFilePaths, String sortMode,
                                                                  Function<String, ? extends T> parser) throws IOException {
        List<IndexedBufferedReader<T>> readers = new ArrayList<>();

        for (int i = 0; i < chunkFilePaths.size(); i++) {
            String chunkFilePath = chunkFilePaths.get(i);
            BufferedReader reader = new BufferedReader(new FileReader(chunkFilePath));
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
            int minIndex = -1;
            T minValue = null;

            for (int i = 0; i < readers.size(); i++) {
                IndexedBufferedReader<T> reader = readers.get(i);
                T currentItem = reader.currentItem;
                if (currentItem != null && (minIndex == -1 ||
                        (sortMode.equals(SORT_MODE_ASCENDING) && currentItem.compareTo(minValue) < 0) ||
                        (sortMode.equals(SORT_MODE_DESCENDING) && currentItem.compareTo(minValue) > 0))) {
                    minIndex = i;
                    minValue = currentItem;
                }
            }

            if (minIndex != -1) {
                sortedData.add(minValue);
                IndexedBufferedReader<T> minReader = readers.get(minIndex);
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
