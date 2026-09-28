package com.smartcityx.algorithm.dp;

/**
 * Matrix Chain Multiplication DP.
 * Time: O(n^3), Space: O(n^2)
 * Used in SmartCityX to optimize sequences of analytics operations.
 */
public class MatrixChainDP {

    public static class MatrixChainResult {
        public final int minCost;
        public final int[][] dpTable;
        public final String optimalParenthesization;
        public final long executionTimeMs;

        public MatrixChainResult(int minCost, int[][] dpTable, String optimalParenthesization, long executionTimeMs) {
            this.minCost = minCost;
            this.dpTable = dpTable;
            this.optimalParenthesization = optimalParenthesization;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Solve matrix chain multiplication.
     *
     * @param dims array of matrix dimensions: dims[i] x dims[i+1] for matrix i.
     */
    public static MatrixChainResult solve(int[] dims) {
        long start = System.currentTimeMillis();

        int n = dims.length - 1; // number of matrices
        int[][] dp = new int[n][n];
        int[][] split = new int[n][n];

        // l is the chain length
        for (int l = 2; l <= n; l++) {
            for (int i = 0; i <= n - l; i++) {
                int j = i + l - 1;
                dp[i][j] = Integer.MAX_VALUE;
                for (int k = i; k < j; k++) {
                    int cost = dp[i][k] + dp[k + 1][j] + dims[i] * dims[k + 1] * dims[j + 1];
                    if (cost < dp[i][j]) {
                        dp[i][j] = cost;
                        split[i][j] = k;
                    }
                }
            }
        }

        String parens = n > 0 ? parenthesize(split, 0, n - 1) : "";
        long elapsed = System.currentTimeMillis() - start;
        return new MatrixChainResult(n > 1 ? dp[0][n - 1] : 0, dp, parens, elapsed);
    }

    private static String parenthesize(int[][] split, int i, int j) {
        if (i == j) return "M" + (i + 1);
        int k = split[i][j];
        return "(" + parenthesize(split, i, k) + " × " + parenthesize(split, k + 1, j) + ")";
    }
}
