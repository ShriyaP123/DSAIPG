package com.phasmidsoftware.dsaipg.util;

import java.util.*;
import java.util.function.Supplier;

import com.phasmidsoftware.dsaipg.adt.pq.PQException;
import com.phasmidsoftware.dsaipg.adt.pq.PriorityQueue;

public class Heap_Comparisons {
    private static final int INSERTIONS = 16000;
    private static final int REMOVALS = 4000;
    private static final int M = 4095;

    public static void main(String[] args) {
        Comparator<Integer> comparator = Integer::compare;

        // Benchmarking different types of priority queues
        benchmarkPriorityQueue("Binary Heap", () -> new PriorityQueue<>(M, false, comparator, false));
        benchmarkPriorityQueue("Binary Heap with Floyd's Trick", () -> new PriorityQueue<>(M, false, comparator, true));
        benchmarkPriorityQueue("4-ary Heap", () -> new PriorityQueue<>(M, false, comparator, false)); // Assuming 4-ary heap uses similar implementation
        benchmarkPriorityQueue("4-ary Heap with Floyd's Trick", () -> new PriorityQueue<>(M, false, comparator, true));

        // Benchmark Fibonacci Heap
        benchmarkFibonacciHeap();
    }

    private static void benchmarkPriorityQueue(String name, Supplier<PriorityQueue<Integer>> supplier) {
        PriorityQueue<Integer> pq = supplier.get();
        Random random = new Random();
        final Integer[] maxSpilled = {null};
        Comparator<Integer> comparator = Integer::compare;

        // Create the Benchmark_Timer and run the benchmark
        Benchmark_Timer<Void> benchmark = new Benchmark_Timer<>(name, unused -> {
            for (int i = 0; i < INSERTIONS; i++) {
                if (pq.size() >= M) {
                    Integer spilled;
                    try {
                        spilled = pq.take();
                    } catch (PQException e) {
                        throw new RuntimeException(e);
                    }
                    if (maxSpilled[0] == null || comparator.compare(spilled, maxSpilled[0]) > 0) {
                        maxSpilled[0] = spilled;
                    }
                }
                pq.give(random.nextInt());
            }
            for (int i = 0; i < REMOVALS; i++) {
                try {
                    pq.take();
                } catch (PQException e) {
                    throw new RuntimeException(e);
                }
            }
        });

        // Run the benchmark and record the time
        double time = benchmark.run(null, 10);  // Number of iterations can be adjusted
        System.out.printf("%s: %.3f ms (Highest spilled: %d)%n", name, time, maxSpilled[0]);

        // Clear the heap after the benchmark
        clearHeap(pq);
    }

    private static void clearHeap(PriorityQueue<Integer> pq) {
        try {
            // Remove all elements from the priority queue
            while (pq.size() > 0) {
                pq.take();  // Remove the element
            }
        } catch (PQException e) {
            throw new RuntimeException("Error clearing the priority queue", e);
        }
    }

    private static void benchmarkFibonacciHeap() {
        try {
            FibonnaciHeap fibonacciHeap = new FibonnaciHeap();
            Random random = new Random();
            final Integer[] maxSpilled = {null};

            Benchmark_Timer<Void> benchmark = new Benchmark_Timer<>(
                    "Fibonacci Heap",
                    unused -> {
                        for (int i = 0; i < INSERTIONS; i++) {
                            if (fibonacciHeap.size() >= M) {
                                int spilled = fibonacciHeap.removeMin();
                                if (maxSpilled[0] == null || spilled > maxSpilled[0]) {
                                    maxSpilled[0] = spilled;
                                }
                            }
                            fibonacciHeap.insert(random.nextInt());
                        }
                        for (int i = 0; i < REMOVALS; i++) {
                            fibonacciHeap.removeMin(); // Remove minimums gracefully
                        }
                    }
            );

            // Run the benchmark
            double time = benchmark.run(null, 10);  // Number of iterations can be adjusted
            System.out.printf("Fibonacci Heap: %.3f ms (Highest spilled: %d)%n", time, maxSpilled[0]);

            // Clear the Fibonacci Heap after the benchmark
            clearFibonacciHeap(fibonacciHeap);

        } catch (OutOfMemoryError e) {
            System.err.println("OutOfMemoryError: Unable to allocate more memory for Fibonacci Heap");
            System.exit(0);  // Stop the program or handle in another way
        }
    }

    private static void clearFibonacciHeap(FibonnaciHeap fibonacciHeap) {
        // Remove all elements from the Fibonacci heap
        while (fibonacciHeap.size() > 0) {
            fibonacciHeap.removeMin();  // Remove the minimum element
        }
    }
}
