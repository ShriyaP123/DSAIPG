package com.phasmidsoftware.dsaipg.util.benchmark;

import com.phasmidsoftware.dsaipg.sort.elementary.HeapSort;
import com.phasmidsoftware.dsaipg.sort.generic.SortWithHelper;
import com.phasmidsoftware.dsaipg.sort.helper.Helper;
import com.phasmidsoftware.dsaipg.sort.helper.HelperFactory;
import com.phasmidsoftware.dsaipg.sort.linearithmic.MergeSort;
import com.phasmidsoftware.dsaipg.util.config.Config;
import com.phasmidsoftware.dsaipg.util.logging.LazyLogger;

import java.io.IOException;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;

import static com.phasmidsoftware.dsaipg.util.benchmark.SortBenchmark.runStringSortBenchmark;
import static com.phasmidsoftware.dsaipg.util.general.Utilities.formatWhole;

/**
 * Class to extend Benchmark_Timer for sorting an array of T values.
 * The default implementation of run in this class randomly selects a subset of the array to be sorted.
 * Each sort is preceded (optionally) by a preProcessor and succeeded (optionally) by a postProcessor.
 *
 * @param <T> the underlying type to be sorted.
 */
public class SorterBenchmark<T extends Comparable<T>> extends Benchmark_Timer<T[]> {

    // Logger for logging benchmarking information
    private final static LazyLogger logger = new LazyLogger(SorterBenchmark.class);

    protected final SortWithHelper<T> sorter;
    protected  T[] ts;
    protected final int nRuns;
    protected final TimeLogger[] timeLoggers;
    private final Class<T> tClass;
    private Config config;

    /**
     * Run a benchmark on a sorting problem with N elements.
     *
     * @param description the description of the task being timed.
     * @param N           the number of elements.
     */
    public void run(String description, int N) {
        if (nRuns > 0) {
            logger.info("run: sort " + formatWhole(N) + " elements with " + this);
            sorter.init(N);
            final double time = super.runFromSupplier(() -> generateRandomArray(ts), nRuns);
            for (TimeLogger timeLogger : timeLoggers) timeLogger.log(description, time, N);
        } else {
            logger.warn("run: skipping " + this);
        }
    }

    /**
     * Returns a string representation of the SorterBenchmark object, including the class type,
     * total number of elements, and number of runs.
     *
     * @return a string containing the description of the SorterBenchmark instance.
     */
    @Override
    public String toString() {
        return "SorterBenchmark on " + tClass + " from " + formatWhole(ts.length) + " total elements and " + formatWhole(nRuns) + " runs";
    }

    /**
     * Constructor for a SorterBenchmark where we provide the following parameters:
     *
     * @param tClass        the class of T.
     * @param preProcessor  an optional pre-processor which is applied before each sort.
     * @param sorter        the sorter.
     * @param postProcessor an optional post-processor which is applied after each sort.
     * @param ts            the array of Ts.
     * @param nRuns         the number of runs to perform in this benchmark.
     * @param timeLoggers   the time-loggers.
     */
    public SorterBenchmark(Class<T> tClass, UnaryOperator<T[]> preProcessor, SortWithHelper<T> sorter, Consumer<T[]> postProcessor, T[] ts, int nRuns, TimeLogger[] timeLoggers) {
        super(sorter.toString(), preProcessor, sorter::mutatingSort, postProcessor);
        this.sorter = sorter;
        this.tClass = tClass;
        this.ts = ts;
        this.nRuns = nRuns;
        this.timeLoggers = timeLoggers;
    }

    /**
     * Constructor for a SorterBenchmark where we provide the following parameters:
     * For this form of the constructor, the post-processor always checks that the sort was successful.
     *
     * @param tClass       the class of T.
     * @param preProcessor an optional pre-processor which is applied before each sort.
     * @param sorter       the sorter.
     * @param ts           the array of Ts.
     * @param nRuns        the number of runs to perform in this benchmark.
     * @param timeLoggers  the time-loggers.
     */
    public SorterBenchmark(Class<T> tClass, UnaryOperator<T[]> preProcessor, SortWithHelper<T> sorter, T[] ts, int nRuns, TimeLogger[] timeLoggers) {
        this(tClass, preProcessor, sorter, sorter::postProcess, ts, nRuns, timeLoggers);
    }

    /**
     * Constructor for a SorterBenchmark where we provide the following parameters:
     * For this form of the constructor, the post-processor always checks that the sort was successful.
     * For this form of the constructor, there is no pre-processor.
     *
     * @param tClass      the class of T.
     * @param sorter      the sorter.
     * @param ts          the array of Ts.
     * @param nRuns       the number of runs to perform in this benchmark.
     * @param timeLoggers the time-loggers.
     */
    public SorterBenchmark(Class<T> tClass, SortWithHelper<T> sorter, T[] ts, int nRuns, TimeLogger[] timeLoggers) {
        this(tClass, null, sorter, ts, nRuns, timeLoggers);
    }

    /**
     * Generates a random array of type T based on a given lookup array.
     *
     * @param lookupArray the array of elements that will be used as the source for generating random values.
     * @return a new array of randomly selected elements of type T, with values chosen from the lookup array.
     */
    private T[] generateRandomArray(T[] lookupArray) {
        Random random = new Random();
        if (lookupArray.length == 0) {
            return (T[]) java.lang.reflect.Array.newInstance(tClass, 3);
        }

        T[] randomArray = (T[]) java.lang.reflect.Array.newInstance(tClass, lookupArray.length);

        for (int i = 0; i < lookupArray.length; i++) {

            randomArray[i] = lookupArray[random.nextInt(lookupArray.length)];
        }

        return randomArray;
    }

    /**
     * Method to check if a given sorter is configured in the benchmark config.
     *
     * @param sorterName the name of the sorter.
     * @return true if the sorter is configured in the benchmark, false otherwise.
     */
    private boolean isConfigBenchmarkStringSorter(String sorterName) {
        // This method should check if the sorter is enabled in the config.ini file.
        return sorterName.equals("heapsort"); // Example for checking heapsort
    }

    /**
     * Runs a benchmark for the specified sorting algorithm (heapsort).
     */

    public void runHeapsortBenchmark() {
        if (isConfigBenchmarkStringSorter("heapsort")) {
            try {
               config = Config.load(MergeSort.class);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Helper<String> helper = HelperFactory.create("Heapsort", ts.length, config);
            String[] stringArray = new String[ts.length];
            for (int i = 0; i < ts.length; i++) {
                stringArray[i] = ts[i].toString();
            }
            runStringSortBenchmark(stringArray, ts.length, nRuns, new HeapSort<>(helper), timeLoggers);
        }
    }
}
