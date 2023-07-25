package ru.cft.team.filemerge;

import lombok.extern.slf4j.Slf4j;
import ru.cft.team.filemerge.exception.ValidationException;
import ru.cft.team.filemerge.util.ArgumentParser;
import ru.cft.team.filemerge.util.MergeSort;

import java.io.IOException;
import java.util.List;

import static ru.cft.team.filemerge.util.Constant.*;

@Slf4j
public class FileMergeSorterApp {

    public static void main(String[] args) {
        List<String> arguments = List.copyOf(List.of(args));

        if (arguments.size() < 2) {
            log.error("Insufficient arguments passed." + arguments);
            throw new ValidationException(
                    "Usage: java FileMergeSort <-a/-d> <-s/-i> <output_file> <input_files...>");
        }

        ArgumentParser argumentParser = new ArgumentParser();
        argumentParser.parse(arguments);

        try {
            sortData(argumentParser.getSortMode(), argumentParser.getDataType(),
                    argumentParser.getInputFilePaths(), argumentParser.getOutputFile());
        } catch (IOException e) {
            log.error("Error while sorting files.", e);
            throw new ValidationException("Error while sorting files. " + e);
        }
    }

    private static void sortData(String sortMode, String dataType,
                                 List<String> filePaths, String outputFile) throws IOException {

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
