package com.smartcityx.algorithm.dp;

import java.util.ArrayList;
import java.util.List;

/**
 * Bitmask DP for optimal subset selection.
 * Used in SmartCityX to evaluate combinations of city resources for task coverage.
 * Time: O(2^n * n), Space: O(2^n)
 */
public class BitmaskDP {

    public static class BitmaskResult {
        public final int minCost;
        public final List<Integer> selectedItems;
        public final int[] dpArray;
        public final long executionTimeMs;

        public BitmaskResult(int minCost, List<Integer> selectedItems, int[] dpArray, long executionTimeMs) {
            this.minCost = minCost;
            this.selectedItems = selectedItems;
            this.dpArray = dpArray;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Given n items with costs, find the minimum-cost subset that covers all required tasks.
     * tasks[i] is a bitmask of tasks item i covers.
     *
     * @param n       number of items (max 20 for tractability)
     * @param costs   cost of each item
     * @param tasks   bitmask of tasks each item covers
     * @param allTasks bitmask of all tasks that must be covered
     */
    public static BitmaskResult solve(int n, int[] costs, int[] tasks, int allTasks) {
        long start = System.currentTimeMillis();

        int states = 1 << n;
        int[] dp = new int[allTasks + 1];
        int[] from = new int[allTasks + 1];

        java.util.Arrays.fill(dp, Integer.MAX_VALUE / 2);
        java.util.Arrays.fill(from, -1);
        dp[0] = 0;

        // Iterate over subsets of items
        for (int mask = 0; mask < states; mask++) {
            // compute coverage and cost for this item subset
            int coverage = 0, cost = 0;
            for (int i = 0; i < n; i++) {
                if ((mask & (1 << i)) != 0) {
                    coverage |= tasks[i];
                    cost += costs[i];
                }
            }
            if (coverage == allTasks && cost < dp[allTasks]) {
                dp[allTasks] = cost;
                from[allTasks] = mask;
            }
        }

        // Recover selected items from the best subset mask
        List<Integer> selected = new ArrayList<>();
        if (from[allTasks] != -1) {
            int bestMask = from[allTasks];
            for (int i = 0; i < n; i++) {
                if ((bestMask & (1 << i)) != 0) selected.add(i + 1);
            }
        }

        // Return a limited dp array for display
        int[] displayDp = new int[Math.min(dp.length, 64)];
        for (int i = 0; i < displayDp.length; i++) displayDp[i] = dp[i] >= Integer.MAX_VALUE / 2 ? -1 : dp[i];

        long elapsed = System.currentTimeMillis() - start;
        return new BitmaskResult(dp[allTasks] >= Integer.MAX_VALUE / 2 ? -1 : dp[allTasks], selected, displayDp, elapsed);
    }
}
