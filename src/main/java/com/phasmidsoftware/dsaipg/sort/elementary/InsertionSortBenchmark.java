package com.phasmidsoftware.dsaipg.sort.elementary;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class InsertionSortBenchmark {

    public static void main(String[] args) {
        int[] sizes = {1000, 2000, 4000, 8000, 16000};
        Random rand = new Random();
        InsertionSortBasic<Integer> sorter = InsertionSortBasic.create();

        // Run benchmarks for different types of initial array orderings
        for (int n : sizes) {
            System.out.println("Array size: " + n);

            // Test for random order
            Integer[] randomArray = generateRandomArray(n, rand);
            benchmarkSort("Random", randomArray, sorter);

            // Test for ordered (sorted) array
            Integer[] orderedArray = Arrays.copyOf(randomArray, randomArray.length);
            Arrays.sort(orderedArray);
            benchmarkSort("Ordered", orderedArray, sorter);

            // Test for partially-ordered array (first half sorted)
            Integer[] partiallyOrderedArray = Arrays.copyOf(randomArray, randomArray.length);
            Arrays.sort(partiallyOrderedArray, 0, n / 2);
            benchmarkSort("Partially Ordered", partiallyOrderedArray, sorter);

            // Test for reverse-ordered array
            Integer[] reverseOrderedArray = Arrays.copyOf(randomArray, randomArray.length);
            Arrays.sort(reverseOrderedArray, Comparator.reverseOrder());
            benchmarkSort("Reverse Ordered", reverseOrderedArray, sorter);

            System.out.println();
        }
    }

    // Method to generate a random array of integers
    private static Integer[] generateRandomArray(int size, Random rand) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = rand.nextInt(size);
        }
        return array;
    }

    // Method to run a benchmark on a specific type of array
    private static void benchmarkSort(String label, Integer[] array, InsertionSortBasic<Integer> sorter) {
        long startTime = System.nanoTime();
        sorter.sort(array); // Sort the array
        long endTime = System.nanoTime();
        long duration = endTime - startTime;
        System.out.println(label + " array sorting took: " + duration + " nanoseconds");
    }
}

