package ru.cft.team.filemerge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.team.filemerge.util.CustomFileWriter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static ru.cft.team.filemerge.Constant.TEST_OUTPUT_DIRECTORY;

class CustomFileWriterTests {

    @Test
    void testWriteIntegerData() throws IOException {
        String filePath = TEST_OUTPUT_DIRECTORY + "integers_output.txt";
        List<Integer> sortedData = Arrays.asList(1, 2, 3, 4, 5);

        CustomFileWriter.write(filePath, sortedData);

        Path path = Paths.get(filePath);
        List<String> actualData = Files.readAllLines(path);
        List<String> expectedData = Arrays.asList("1", "2", "3", "4", "5");

        Assertions.assertEquals(expectedData, actualData);

        Files.deleteIfExists(path);
    }

    @Test
    void testWriteStringData() throws IOException {
        String fileName = "strings_output.txt";
        String filePath = TEST_OUTPUT_DIRECTORY + fileName;
        List<String> sortedData = Arrays.asList("apple", "banana", "cherry", "grape", "orange");

        Path path = Paths.get(filePath);
        try {
            CustomFileWriter.write(filePath, sortedData);

            List<String> actualData = Files.readAllLines(path);
            List<String> expectedData = Arrays.asList("apple", "banana", "cherry", "grape", "orange");

            Assertions.assertEquals(expectedData, actualData);
        } finally {
            Files.deleteIfExists(path);
        }
    }


    @Test
    void testWriteEmptyData() throws IOException {
        String filePath = TEST_OUTPUT_DIRECTORY + "empty_output.txt";
        List<String> sortedData = List.of();

        Path path = Paths.get(filePath);
        CustomFileWriter.write(filePath, sortedData);

        List<String> actualData = Files.readAllLines(path);
        List<String> expectedData = List.of();

        Assertions.assertEquals(expectedData, actualData);

        Files.deleteIfExists(path);
    }

}
