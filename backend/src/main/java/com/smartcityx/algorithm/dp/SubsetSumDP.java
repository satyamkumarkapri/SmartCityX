package com.smartcityx.algorithm.dp;

import java.util.ArrayList;
import java.util.List;

/**
 * Subset Sum DP.
 * Time: O(n * target), Space: O(n * target)
 * Used in SmartCityX for small city-resource selection problems.
 */
public class SubsetSumDP {

    public static class SubsetSumResult {
        public final boolean possible;
        public final boolean[][] dpTable;
        public final List<Integer> subsetFound;
        public final long executionTimeMs;

        public SubsetSumResult(boolean possible, boolean[][] dpTable, List<Integer> subsetFound, long executionTimeMs) {
            this.possible = possible;
            this.dpTable = dpTable;
            this.subsetFound = subsetFound;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Determine if any subset of nums sums to target.
     * Returns full DP table and the actual subset if found.
     */
    public static SubsetSumResult solve(int[] nums, int target) {
        long start = System.currentTimeMillis();

        int n = nums.length;
        boolean[][] dp = new boolean[n + 1][target + 1];

        // Empty subset sums to 0
        for (int i = 0; i <= n; i++) dp[i][0] = true;

        for (int i = 1; i <= n; i++) {
            for (int j = 0; j <= target; j++) {
                dp[i][j] = dp[i - 1][j];
                if (j >= nums[i - 1] && dp[i - 1][j - nums[i - 1]]) {
                    dp[i][j] = true;
                }
            }
        }

        // Backtrack to find the actual subset
        List<Integer> subset = new ArrayList<>();
        if (dp[n][target]) {
            int i = n, j = target;
            while (i > 0 && j > 0) {
                if (!dp[i - 1][j]) {
                    subset.add(nums[i - 1]);
                    j -= nums[i - 1];
                }
                i--;
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new SubsetSumResult(dp[n][target], dp, subset, elapsed);
    }
}
