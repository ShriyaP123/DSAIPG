/*
 * Copyright (c) 2017-2024. Robin Hillyard
 */

package com.phasmidsoftware.dsaipg.misc;

class NewtonApproximation {
    public static void main(String[] args) {
        // Newton's Approximation to solve sec(x) - x^2 = 0
        double x = 1.0; // Initial guess
        int left = 200; // Maximum iterations
        for (; left > 0; left--) {
            // f(x) = sec(x) - x^2
            final double y = (1 / Math.cos(x)) - x * x;
            if (Math.abs(y) < 1E-7) {
                System.out.println("The solution to sec(x) - x^2 = 0 is: " + x);
                System.exit(0);
            }
            // f'(x) = sec(x)tan(x) - 2x
            final double derivative = Math.tan(x) / Math.cos(x) - 2 * x;
            // Update x using Newton's formula
            x = x - y / derivative;
        }
        // If the loop completes without finding a solution
        System.out.println("No solution found within the maximum number of iterations.");
    }
}