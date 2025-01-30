package com.phasmidsoftware.dsaipg.adt.threesum;

import com.phasmidsoftware.dsaipg.util.Stopwatch;
import java.util.Arrays;

public class ThreeSumTiming {
    public static void main(String[] args) {
        // Example of array sizes
        int[] sizes = {1000, 2000, 4000, 8000, 16000};

        // Test for ThreeSumQuartic
        System.out.println("ThreeSumQuadrithmic:");
        for (int size : sizes) {
            int[] arr = generateRandomArray(size);
            System.out.println("ThreeSumQuadrithmic with getTriple(int i, int j):");
            try (Stopwatch stopwatch = new Stopwatch()) {
                ThreeSumQuadrithmic threeSum = new ThreeSumQuadrithmic(arr);
                threeSum.getTriple(3, 4);  // This is where the time is spent
                System.out.println("N = " + size + ", Time = " + stopwatch.lap() + " ms");
            }
            System.out.println("ThreeSumQuadrithmic with getTriples():");
            try (Stopwatch stopwatch = new Stopwatch()) {
                ThreeSumQuadrithmic threeSum = new ThreeSumQuadrithmic(arr);
                threeSum.getTriples();  // This is where the time is spent
                System.out.println("N = " + size + ", Time = " + stopwatch.lap() + " ms");
            }
        }

        // Test for ThreeSumQuadratic
        System.out.println("ThreeSumQuadratic:");
        for (int size : sizes) {
            int[] arr = generateRandomArray(size);
            System.out.println("ThreeSumQuadratic with getTriples():");
            try (Stopwatch stopwatch = new Stopwatch()) {
                ThreeSumQuadratic threeSum = new ThreeSumQuadratic(arr);
                threeSum.getTriples();  // This is where the time is spent
                System.out.println("N = " + size + ", Time = " + stopwatch.lap() + " ms");
            }
            System.out.println("ThreeSumQuadratic with getTriples(int j):");
            try (Stopwatch stopwatch = new Stopwatch()) {
                ThreeSumQuadratic threeSum = new ThreeSumQuadratic(arr);
                threeSum.getTriples(3);  // This is where the time is spent
                System.out.println("N = " + size + ", Time = " + stopwatch.lap() + " ms");
            }
        }

        // Test for ThreeSumCubic
        System.out.println("ThreeSumCubic:");
        for (int size : sizes) {
            int[] arr = generateRandomArray(size);
            try (Stopwatch stopwatch = new Stopwatch()) {
                ThreeSumCubic threeSum = new ThreeSumCubic(arr);
                threeSum.getTriples();  // This is where the time is spent
                System.out.println("N = " + size + ", Time = " + stopwatch.lap() + " ms");
            }
        }
    }

    // Utility function to generate a random sorted array
    private static int[] generateRandomArray(int size) {
        int[] arr = new int[size];
        for (int i = 0; i < size; i++) {
            arr[i] = (int) (Math.random() * 10000);  // random integers in a reasonable range
        }
        Arrays.sort(arr);  // Ensure the array is sorted
        return arr;
    }
}
