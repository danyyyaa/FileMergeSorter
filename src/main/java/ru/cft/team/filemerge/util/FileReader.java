package ru.cft.team.filemerge.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.cft.team.filemerge.exception.FileReadException;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

@UtilityClass
@Slf4j
public class FileReader {
    public <T> List<T> read(String path, Function<String, ? extends T> parser) {
        List<T> dataList = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new java.io.FileReader(path))) {
            reader.lines()
                    .filter(line -> !line.isEmpty())
                    .map(parser)
                    .forEach(dataList::add);
        } catch (IOException e) {
            log.error("Error reading data from file: {}", path, e);
            throw new FileReadException("Error reading data from file: " + path);
        }
        return dataList;
    }
}
