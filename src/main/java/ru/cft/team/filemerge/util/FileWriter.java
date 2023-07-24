package ru.cft.team.filemerge.util;

import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import ru.cft.team.filemerge.exception.FileWriteException;

import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Collection;

@UtilityClass
@Slf4j
public class FileWriter {
    public void write(String path, Collection<?> sortedData) {
        try (BufferedWriter writer = new BufferedWriter(new java.io.FileWriter(path))) {
            sortedData.stream()
                    .map(Object::toString)
                    .forEach(data -> {
                        try {
                            writer.write(data);
                            writer.newLine();
                        } catch (IOException e) {
                            log.error("Error writing data to file: {}", path, e);
                            throw new FileWriteException("Error writing data to file: " + path);
                        }
                    });
        } catch (IOException e) {
            log.error("Error writing data to file: {}", path, e);
            throw new FileWriteException("Error writing data to file: " + path);
        }
    }
}
