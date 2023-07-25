package ru.cft.team.filemerge;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import ru.cft.team.filemerge.util.MergeSort;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static ru.cft.team.filemerge.util.Constant.SORT_MODE_ASCENDING;
import static ru.cft.team.filemerge.util.Constant.SORT_MODE_DESCENDING;

class MergeSortTest {

    private static final Function<String, Integer> INTEGER_PARSER = Integer::parseInt;

    @Test
    void testAscendingSort() throws IOException {
        List<String> filePaths = Arrays.asList(
                "src/test/java/ru/cft/team/filemerge/input/in1.txt",
                "src/test/java/ru/cft/team/filemerge/input/in2.txt",
                "src/test/java/ru/cft/team/filemerge/input/in3.txt"
        );
        String outputFilePath = "src/test/java/ru/cft/team/filemerge/output/sorted_files.txt";

        MergeSort.sort(filePaths, SORT_MODE_ASCENDING, INTEGER_PARSER, outputFilePath);

        List<Integer> sortedData = Files.lines(Paths.get(outputFilePath))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Assertions.assertEquals(List.of(1, 2, 3, 4, 5, 6, 7, 8, 9), sortedData);

        Files.deleteIfExists(Paths.get(outputFilePath));
    }

    @Test
    void testDescendingSort() throws IOException {
        List<String> filePaths = Arrays.asList(
                "src/test/java/ru/cft/team/filemerge/input/in1.txt",
                "src/test/java/ru/cft/team/filemerge/input/in2.txt",
                "src/test/java/ru/cft/team/filemerge/input/in3.txt"
        );
        String outputFilePath = "src/test/java/ru/cft/team/filemerge/output/sorted_files.txt";

        MergeSort.sort(filePaths, SORT_MODE_DESCENDING, INTEGER_PARSER, outputFilePath);

        List<Integer> sortedData = Files.lines(Paths.get(outputFilePath))
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        Assertions.assertEquals(List.of(9, 8, 7, 6, 5, 4, 3, 2, 1), sortedData);

        Files.deleteIfExists(Paths.get(outputFilePath));
    }

    @Test
    void testAscendingSortStrings() throws IOException {
        List<String> filePaths = Arrays.asList(
                "src/test/java/ru/cft/team/filemerge/input/in1s.txt",
                "src/test/java/ru/cft/team/filemerge/input/in2s.txt",
                "src/test/java/ru/cft/team/filemerge/input/in3s.txt"
        );
        String outputFilePath = "src/test/java/ru/cft/team/filemerge/output/out.txt";

        MergeSort.sort(filePaths, SORT_MODE_ASCENDING, s -> s, outputFilePath);

        List<String> sortedData = Files.readAllLines(Paths.get(outputFilePath));

        Assertions.assertEquals(
                List.of("apple", "banana", "cherry", "grape", "orange", "pear", "pineapple", "watermelon"), sortedData);

        Files.deleteIfExists(Paths.get(outputFilePath));
    }

    @Test
    void testDescendingSortStrings() throws IOException {
        List<String> filePaths = Arrays.asList(
                "src/test/java/ru/cft/team/filemerge/input/in1s.txt",
                "src/test/java/ru/cft/team/filemerge/input/in2s.txt",
                "src/test/java/ru/cft/team/filemerge/input/in3s.txt"
        );
        String outputFilePath = "src/test/java/ru/cft/team/filemerge/output/out.txt";

        MergeSort.sort(filePaths, SORT_MODE_DESCENDING, s -> s, outputFilePath);

        List<String> sortedData = Files.readAllLines(Paths.get(outputFilePath));

        Assertions.assertEquals(
                List.of("watermelon", "pineapple", "pear", "orange", "grape", "cherry", "banana", "apple"), sortedData);

        Files.deleteIfExists(Paths.get(outputFilePath));
    }
}
