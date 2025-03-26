package com.phasmidsoftware.dsaipg.util.benchmark;

import com.phasmidsoftware.dsaipg.sort.generic.SortWithHelper;
import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.sort.linearithmic.MergeSort;
import com.phasmidsoftware.dsaipg.sort.linearithmic.QuickSort_DualPivot;
import com.phasmidsoftware.dsaipg.sort.elementary.HeapSort;
import com.phasmidsoftware.dsaipg.sort.helper.HelperFactory;
import com.phasmidsoftware.dsaipg.util.config.Config;

import java.io.IOException;

public class BenchmarkFactory {

    private HelperFactory helperFactory;
    private Config config;

    public BenchmarkFactory() {
        // Initialize HelperFactory with a dummy config (no external config loading)
        this.helperFactory = new HelperFactory();
        try {
            this.config = Config.load(); // Default config
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration", e);
        }
    }

    public SorterBenchmark<Integer> createBenchmark(Class<?> sorterClass) {
        try {
            // Dynamically create the appropriate helper and sorter
            Helper helper = createHelper(sorterClass);
            if (helper == null) {
                throw new IllegalArgumentException("No suitable helper found for sorter class: " + sorterClass.getName());
            }

            SortWithHelper<Integer> sorterInstance = createSorterInstance(sorterClass, helper);
            if (sorterInstance == null) {
                throw new IllegalArgumentException("No suitable sorter instance found for sorter class: " + sorterClass.getName());
            }

            // Return the constructed benchmark
            return new SorterBenchmark<>(
                    Integer.class,                   // tClass
                    null,                            // preProcessor (optional)
                    sorterInstance,                  // sorter instance
                    sorterInstance::postProcess,     // postProcessor
                    new Integer[0],                  // Array of integers (sample data or empty)
                    10,                              // Number of runs
                    new TimeLogger[0]                // Time loggers (optional)
            );
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Helper createHelper(Class<?> sorterClass) {
        // Return the appropriate helper based on sorterClass
        if (sorterClass == MergeSort.class) {
            return helperFactory.createGeneric("MergeSort", null, 10, 10, config);
        } else if (sorterClass == QuickSort_DualPivot.class) {
            return helperFactory.createGeneric("QuickSort", null, 10, 10, config);
        } else if (sorterClass == HeapSort.class) {
            return helperFactory.createGeneric("HeapSort", null, 10, 10, config);
        }
        return null; // If no helper is found for this sorter
    }

    private SortWithHelper<Integer> createSorterInstance(Class<?> sorterClass, Helper helper) {
        // Create sorter instance based on the sorter class type
        if (sorterClass == MergeSort.class) {
            return new MergeSort(helper); // MergeSort implements SortWithHelper
        } else if (sorterClass == QuickSort_DualPivot.class) {
            return new QuickSort_DualPivot(helper); // QuickSort_DualPivot implements SortWithHelper
        } else if (sorterClass == HeapSort.class) {
            return new HeapSort(helper); // HeapSort implements SortWithHelper
        }
        return null; // If no matching sorter is found
    }
}
