package ru.cft.team.filemerge;

import ru.cft.team.filemerge.exception.ValidationException;
import ru.cft.team.filemerge.util.FileReader;
import ru.cft.team.filemerge.util.FileWriter;
import ru.cft.team.filemerge.util.MergeSort;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.team.filemerge.util.Constant.*;

public class FileMergeSorterApp {

    public static <T extends Comparable<? super T>> void main(String[] args) {
        //String[] args = {"-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

        if (args.length < 2) {
            throw new ValidationException(
                    "Usage: java FileMergeSort <-a/-d> <-s/-i> <output_file> <input_files...>");
        }

        int counter = 1;

        String sortMode = DATA_MODE_STRING;
        String dataType = null;
        String outputFile = "output.txt";

        try {
            for (int i = 0; i < 3; i++) {
                if (args[i].equals(SORT_MODE_DESCENDING) || args[i].equals(SORT_MODE_ASCENDING)) {
                    sortMode = args[i];
                }
                if (args[i].equals(DATA_MODE_STRING) || args[i].equals(DATA_MODE_INTEGER)) {
                    dataType = args[i];
                }
                if (args[i].contains(".txt")) {
                    outputFile = args[i];
                    break;
                }
                counter++;
            }
        } catch (IllegalArgumentException e) {
            throw new ValidationException("Error. Insufficient arguments passed");
        }

        List<String> data = Arrays.asList(args).subList(counter, args.length)
                .stream()
                .flatMap(fileName -> FileReader.read(INPUT_DIRECTORY + fileName).stream())
                .collect(Collectors.toList());

        if (dataType.equals(DATA_MODE_INTEGER)) {
            List<Integer> intData = data.stream()
                    .map(Integer::valueOf)
                    .collect(Collectors.toList());

            FileWriter.write(OUTPUT_DIRECTORY + outputFile, MergeSort.sort(intData, sortMode));
        } else if (dataType.equals(DATA_MODE_STRING)) {
            FileWriter.write(OUTPUT_DIRECTORY + outputFile, MergeSort.sort(data, sortMode));
        } else {
            throw new ValidationException("Invalid data type. Please use -s for strings or -i for integers.");
        }
    }
}
