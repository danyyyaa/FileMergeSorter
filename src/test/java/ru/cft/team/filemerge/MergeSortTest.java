package ru.cft.team.filemerge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.team.filemerge.util.MergeSort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.cft.team.filemerge.Constant.TEST_INPUT_DIRECTORY;
import static ru.cft.team.filemerge.Constant.TEST_OUTPUT_DIRECTORY;
import static ru.cft.team.filemerge.util.Constant.SORT_MODE_ASCENDING;
import static ru.cft.team.filemerge.util.Constant.SORT_MODE_DESCENDING;

class MergeSortTest {

    private static final Function<String, Integer> INTEGER_PARSER = Integer::parseInt;

    @Test
    void testAscendingSort() throws IOException {
        List<String> filePaths = Arrays.asList(
                TEST_INPUT_DIRECTORY + "in1.txt",
                TEST_INPUT_DIRECTORY + "in2.txt",
                TEST_INPUT_DIRECTORY + "in3.txt"
        );
        String outputFilePath = TEST_OUTPUT_DIRECTORY + "sorted_files.txt";

        MergeSort.sort(filePaths, SORT_MODE_ASCENDING, INTEGER_PARSER, outputFilePath);

        Path path = Paths.get(outputFilePath);
        List<Integer> sortedData;

        try (Stream<String> lines = Files.lines(path)) {
            sortedData = lines.map(Integer::parseInt).collect(Collectors.toList());
        }

        Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), sortedData);

        Files.deleteIfExists(path);
    }

    @Test
    void testDescendingSort() throws IOException {
        List<String> filePaths = Arrays.asList(
                TEST_INPUT_DIRECTORY + "in1.txt",
                TEST_INPUT_DIRECTORY + "in2.txt",
                TEST_INPUT_DIRECTORY + "in3.txt"
        );
        String outputFilePath = TEST_OUTPUT_DIRECTORY + "sorted_files.txt";

        MergeSort.sort(filePaths, SORT_MODE_DESCENDING, INTEGER_PARSER, outputFilePath);

        Path path = Paths.get(outputFilePath);
        List<Integer> sortedData;

        try (Stream<String> lines = Files.lines(path)) {
            sortedData = lines.map(Integer::parseInt).collect(Collectors.toList());
        }

        Assertions.assertEquals(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1), sortedData);

        Files.deleteIfExists(path);
    }


    @Test
    void testAscendingSortStrings() throws IOException {
        List<String> filePaths = Arrays.asList(
                TEST_INPUT_DIRECTORY + "in1s.txt",
                TEST_INPUT_DIRECTORY + "in2s.txt",
                TEST_INPUT_DIRECTORY + "in3s.txt"
        );
        String outputFilePath = TEST_OUTPUT_DIRECTORY + "out.txt";

        MergeSort.sort(filePaths, SORT_MODE_ASCENDING, s -> s, outputFilePath);

        Path path = Paths.get(outputFilePath);
        List<String> sortedData;

        try (Stream<String> lines = Files.lines(path)) {
            sortedData = lines.collect(Collectors.toList());
        }

        Assertions.assertEquals(
                List.of("apple", "banana", "cherry", "grape", "orange", "pear", "pineapple", "watermelon"), sortedData);

        Files.deleteIfExists(path);
    }

    @Test
    void testDescendingSortStrings() throws IOException {
        List<String> filePaths = Arrays.asList(
                TEST_INPUT_DIRECTORY + "in1s.txt",
                TEST_INPUT_DIRECTORY + "in2s.txt",
                TEST_INPUT_DIRECTORY + "in3s.txt"
        );
        String outputFilePath = TEST_OUTPUT_DIRECTORY + "out.txt";

        MergeSort.sort(filePaths, SORT_MODE_DESCENDING, s -> s, outputFilePath);

        Path path = Paths.get(outputFilePath);
        List<String> sortedData;

        try (Stream<String> lines = Files.lines(path)) {
            sortedData = lines.collect(Collectors.toList());
        }

        Assertions.assertEquals(
                List.of("watermelon", "pineapple", "pear", "orange", "grape", "cherry", "banana", "apple"), sortedData);

        Files.deleteIfExists(path);
    }
}
