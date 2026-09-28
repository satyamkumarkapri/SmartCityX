package com.smartcityx.algorithm.dp;

/**
 * Levenshtein (Edit) Distance and Damerau-Levenshtein Distance.
 * Used in SmartCityX to correct citizen query typos.
 */
public class EditDistance {

    public static class EditResult {
        public final int distance;
        public final int[][] dpMatrix;
        public final String suggestion;
        public final long executionTimeMs;

        public EditResult(int distance, int[][] dpMatrix, String suggestion, long executionTimeMs) {
            this.distance = distance;
            this.dpMatrix = dpMatrix;
            this.suggestion = suggestion;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Standard Levenshtein distance with full DP table.
     */
    public static EditResult levenshtein(String a, String b) {
        long start = System.currentTimeMillis();

        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                dp[i][j] = Math.min(
                        Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1),
                        dp[i - 1][j - 1] + cost
                );
            }
        }

        String suggestion = dp[m][n] <= 3 ? b : "No suggestion (distance too large)";
        long elapsed = System.currentTimeMillis() - start;
        return new EditResult(dp[m][n], dp, suggestion, elapsed);
    }

    /**
     * Damerau-Levenshtein distance (also handles transpositions).
     */
    public static EditResult damerauLevenshtein(String a, String b) {
        long start = System.currentTimeMillis();

        int m = a.length(), n = b.length();
        int[][] dp = new int[m + 2][n + 2];
        int maxDist = m + n;

        dp[0][0] = maxDist;
        for (int i = 0; i <= m; i++) {
            dp[i + 1][0] = maxDist;
            dp[i + 1][1] = i;
        }
        for (int j = 0; j <= n; j++) {
            dp[0][j + 1] = maxDist;
            dp[1][j + 1] = j;
        }

        int[] da = new int[256];

        for (int i = 1; i <= m; i++) {
            int db = 0;
            for (int j = 1; j <= n; j++) {
                int i1 = da[b.charAt(j - 1)];
                int j1 = db;
                int cost = a.charAt(i - 1) == b.charAt(j - 1) ? 0 : 1;
                if (cost == 0) db = j;
                dp[i + 1][j + 1] = Math.min(
                        Math.min(dp[i][j] + cost, dp[i + 1][j] + 1),
                        Math.min(dp[i][j + 1] + 1,
                                dp[i1][j1] + (i - i1 - 1) + 1 + (j - j1 - 1))
                );
            }
            da[a.charAt(i - 1)] = i;
        }

        // Extract visible sub-matrix for display
        int[][] visible = new int[m + 1][n + 1];
        for (int i = 0; i <= m; i++)
            for (int j = 0; j <= n; j++)
                visible[i][j] = dp[i + 1][j + 1];

        String suggestion = visible[m][n] <= 3 ? b : "No suggestion";
        long elapsed = System.currentTimeMillis() - start;
        return new EditResult(visible[m][n], visible, suggestion, elapsed);
    }
}
