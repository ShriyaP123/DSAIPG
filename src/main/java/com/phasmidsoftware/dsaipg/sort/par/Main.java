/*
 * Copyright (c) 2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.sort.par;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ForkJoinPool;

/**
 * This code has been fleshed out by Ziyao Qiao. Thanks very much.
 * CONSIDER tidy it up a bit.
 */
public class Main {

    public static void main(String[] args) {
        processArgs(args);
        System.out.println("Degree of parallelism: " + ForkJoinPool.getCommonPoolParallelism());
        Random random = new Random();

        // Define array sizes to test
        int[] arraySizes = {100000, 500000, 1000000, 2000000};

        ArrayList<Long> timeList = new ArrayList<>();

        // Loop over different array sizes
        for (int arraySize : arraySizes) {
            int[] array = new int[arraySize];
            System.out.println("Array size: " + arraySize);

            for (int j = 50; j < 100; j++) {
                ParSort.cutoff = 10000 * (j + 1);

                long time;
                long startTime = System.currentTimeMillis();


                for (int t = 0; t < 10; t++) {
                    for (int i = 0; i < array.length; i++) {
                        array[i] = random.nextInt(10000000);
                    }
                    ParSort.sort(array, 0, array.length);
                }

                long endTime = System.currentTimeMillis();
                time = (endTime - startTime);
                timeList.add(time);

                System.out.println("cutoff：" + (ParSort.cutoff) + "\t\t10times Time:" + time + "ms");
            }
        }


        try {
            FileOutputStream fis = new FileOutputStream("result.csv");
            OutputStreamWriter isr = new OutputStreamWriter(fis);
            BufferedWriter bw = new BufferedWriter(isr);
            int j = 0;
            if (timeList.isEmpty()) {
                System.out.println("No data to write to the file.");
            } else {
                for (long i : timeList) {
                    String content = (double) 10000 * (j + 1) / 2000000 + "," + (double) i / 10 + "\n";
                    j++;
                    bw.write(content);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processArgs(String[] args) {
        String[] xs = args;
        while (xs.length > 0)
            if (xs[0].startsWith("-")) xs = processArg(xs);
    }

    private static String[] processArg(String[] xs) {
        String[] result = new String[0];
        System.arraycopy(xs, 2, result, 0, xs.length - 2);
        processCommand(xs[0], xs[1]);
        return result;
    }

    private static void processCommand(String x, String y) {
        if (x.equalsIgnoreCase("N")) setConfig(x, Integer.parseInt(y));
        else
        if (x.equalsIgnoreCase("P")) //noinspection ResultOfMethodCallIgnored
            ForkJoinPool.getCommonPoolParallelism();
    }

    private static void setConfig(String x, int i) {
        configuration.put(x, i);
    }

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private static final Map<String, Integer> configuration = new HashMap<>();
}