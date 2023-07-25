package ru.cft.team.filemerge;

import lombok.extern.slf4j.Slf4j;
import ru.cft.team.filemerge.exception.ValidationException;
import ru.cft.team.filemerge.util.MergeSort;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.team.filemerge.util.Constant.*;

@Slf4j
public class FileMergeSorterApp {
    public static void main(String[] args1) {
        String[] args = {"-d", "-i", "out.txt", "in1.txt", "in2.txt", "in3.txt"};

        List<String> arguments = List.copyOf(List.of(args));

        if (arguments.size() < 2) {
            log.error("Insufficient arguments passed." +
                    " Usage: java FileMergeSort <-a/-d> <-s/-i> <output_file> <input_files...>");
            throw new ValidationException(
                    "Usage: java FileMergeSort <-a/-d> <-s/-i> <output_file> <input_files...>");
        }

        int counter = 1;

        String sortMode = SORT_MODE_ASCENDING;
        String dataType = null;
        String outputFile = "output.txt";

        try {
            for (int i = 0; i < 3; i++) {
                if (arguments.get(i).equals(SORT_MODE_DESCENDING) || arguments.get(i).equals(SORT_MODE_ASCENDING)) {
                    sortMode = arguments.get(i);
                }
                if (arguments.get(i).equals(DATA_MODE_STRING) || arguments.get(i).equals(DATA_MODE_INTEGER)) {
                    dataType = arguments.get(i);
                }
                if (arguments.get(i).contains(".txt")) {
                    outputFile = arguments.get(i);
                    break;
                }
                counter++;
            }
        } catch (IllegalArgumentException e) {
            log.error("Error parsing arguments.", e);
            throw new ValidationException("Error. Insufficient arguments passed");
        }

        List<String> filePaths = arguments.subList(counter, arguments.size()).stream()
                .map(path -> INPUT_DIRECTORY + path)
                .collect(Collectors.toList());

        assert dataType != null;
        if (dataType.equals(DATA_MODE_INTEGER)) {
            MergeSort.sort(filePaths, sortMode, Integer::valueOf, OUTPUT_DIRECTORY + outputFile);
        } else if (dataType.equals(DATA_MODE_STRING)) {
            MergeSort.sort(filePaths, sortMode, s -> s, OUTPUT_DIRECTORY + outputFile);
        } else {
            log.error("Invalid data type. Please use -s for strings or -i for integers.");
            throw new ValidationException("Invalid data type. Please use -s for strings or -i for integers.");
        }
    }
}
