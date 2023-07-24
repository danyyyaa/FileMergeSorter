
package ru.cft.team.filemerge.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

import static ru.cft.team.filemerge.util.Constant.SORT_MODE_ASCENDING;

@UtilityClass
public class MergeSort {
    public <T extends Comparable<? super T>> List<T> sort(List<T> data, String sortMode) {
        if (data.size() <= 1) {
            return data;
        }

        boolean sorted = true;
        for (int i = 1; i < data.size(); i++) {
            T prev = data.get(i - 1);
            T current = data.get(i);
            int compareResult = (
                    sortMode.equals(SORT_MODE_ASCENDING)) ? prev.compareTo(current) : current.compareTo(prev);
            if (compareResult > 0) {
                sorted = false;
                break;
            }
        }

        if (sorted) {
            return data;
        }

        int mid = data.size() / 2;
        List<T> left = new ArrayList<>(data.subList(0, mid));
        List<T> right = new ArrayList<>(data.subList(mid, data.size()));

        left = sort(left, sortMode);
        right = sort(right, sortMode);

        return merge(left, right, sortMode);
    }

    private <T extends Comparable<? super T>> List<T> merge(List<T> left, List<T> right, String sortMode) {
        List<T> merged = new ArrayList<>();
        int leftIdx = 0;
        int rightIdx = 0;

        while (leftIdx < left.size() && rightIdx < right.size()) {
            T leftElement = left.get(leftIdx);
            T rightElement = right.get(rightIdx);

            int compareResult = (sortMode.equals(SORT_MODE_ASCENDING)) ?
                    leftElement.compareTo(rightElement) : rightElement.compareTo(leftElement);

            if (compareResult <= 0) {
                merged.add(leftElement);
                leftIdx++;
            } else {
                merged.add(rightElement);
                rightIdx++;
            }
        }

        while (leftIdx < left.size()) {
            merged.add(left.get(leftIdx));
            leftIdx++;
        }

        while (rightIdx < right.size()) {
            merged.add(right.get(rightIdx));
            rightIdx++;
        }

        return merged;
    }
}