package com.phasmidsoftware.dsaipg.util.benchmark;

import com.phasmidsoftware.dsaipg.sort.generic.SortWithHelper;
import com.phasmidsoftware.dsaipg.sort.linearithmic.MergeSort;
import com.phasmidsoftware.dsaipg.sort.linearithmic.QuickSort_DualPivot;
import com.phasmidsoftware.dsaipg.sort.elementary.HeapSort;
import com.phasmidsoftware.dsaipg.util.config.Config;

import java.io.IOException;
import java.util.Random;

public class SortBenchMarkTest {

    private static final int[] sizes = {10000, 20000, 40000, 80000, 160000, 256000};
    private static final Random random = new Random();

    public static void main(String[] args) {

        for (int size : sizes) {
            runBenchmark(MergeSort.class, "MergeSort", size);
            runBenchmark(QuickSort_DualPivot.class, "QuickSort", size);
            runBenchmark(HeapSort.class, "HeapSort", size);
        }
    }

    private static SorterBenchmark<Integer> createSorterBenchmark(Class<?> sorterClass, int size) {
        Integer[] data = new Integer[size];
        for (int i = 0; i < size; i++) {
            data[i] = random.nextInt();
        }
        return new SorterBenchmark<>(
                Integer.class,
                null,
                getSorterInstance(sorterClass, size),
                data,
                10,
                new TimeLogger[0]
        );
    }

    private static SortWithHelper<Integer> getSorterInstance(Class<?> sorterClass, int size) {
        if (sorterClass == MergeSort.class) {
            Config config;
            try {
                config = Config.load(MergeSort.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new MergeSort<>(size, 10, config);
        } else if (sorterClass == QuickSort_DualPivot.class) {
            Config config;
            try {
                config = Config.load(QuickSort_DualPivot.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new QuickSort_DualPivot(size, 10, config);
        } else if (sorterClass == HeapSort.class) {
            Config config;
            try {
                config = Config.load(HeapSort.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return new HeapSort(size, 10, config);
        }
        throw new IllegalArgumentException("Unsupported sorter class: " + sorterClass.getName());
    }

    private static void runBenchmark(Class<?> sorterClass, String algorithmName, int size) {

        SorterBenchmark<Integer> benchmark = createSorterBenchmark(sorterClass, size);

        // First run for instrumentation
        benchmark.run(algorithmName, size);

        // Second run for timing
        Timer timer = new Timer();
        double averageTime = timer.repeat(10, () -> {
            benchmark.run(algorithmName, size);
            return null;
        });

        System.out.println(algorithmName + " - Size: " + size + " - Average Time: " + averageTime + " ms");
    }
}
