package ru.cft.team.filemerge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.team.filemerge.exception.ValidationException;
import ru.cft.team.filemerge.util.CustomFileReader;

import java.util.List;

import static ru.cft.team.filemerge.Constant.TEST_INPUT_DIRECTORY;

class CustomFileReaderTests {

    @Test
    void testReadIntegerData() {
        String filePath = TEST_INPUT_DIRECTORY + "integers.txt";
        List<Integer> expectedData = List.of(1, 2, 3, 4, 5);

        List<Integer> actualData = CustomFileReader.read(filePath, Integer::parseInt);

        Assertions.assertEquals(expectedData, actualData);
    }

    @Test
    void testReadStringData() {
        String filePath = TEST_INPUT_DIRECTORY + "strings.txt";
        List<String> expectedData = List.of("apple", "banana", "cherry", "grape", "orange");

        List<String> actualData = CustomFileReader.read(filePath, s -> s);

        Assertions.assertEquals(expectedData, actualData);
    }

    @Test
    void testReadInvalidData() {
        String filePath = TEST_INPUT_DIRECTORY + "/invalid_data.txt";

        Assertions.assertThrows(ValidationException.class, () ->
                CustomFileReader.read(filePath, Integer::parseInt));
    }
}
