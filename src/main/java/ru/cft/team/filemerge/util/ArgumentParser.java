package ru.cft.team.filemerge.util;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.cft.team.filemerge.exception.ValidationException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.cft.team.filemerge.util.Constant.*;

@Getter
@Slf4j
@NoArgsConstructor
public class ArgumentParser {
    private String sortMode = SORT_MODE_ASCENDING;
    private String dataType = null;
    private String outputFile = "output.txt";
    private List<String> inputFilePaths;

    public void parse(List<String> arguments) {
        int counter = 1;

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

        inputFilePaths = arguments.subList(counter, arguments.size()).stream()
                .map(path -> INPUT_DIRECTORY + path)
                .collect(Collectors.toList());

        if (dataType == null) {
            log.error("Invalid data type. Please use -s for strings or -i for integers.");
            throw new ValidationException("Invalid data type. Please use -s for strings or -i for integers.");
        }
    }
}
