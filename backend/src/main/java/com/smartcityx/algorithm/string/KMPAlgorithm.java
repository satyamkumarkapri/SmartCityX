package com.smartcityx.algorithm.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Knuth-Morris-Pratt string searching algorithm.
 * Time: O(n + m), Space: O(m)
 * Used in SmartCityX to search citizen reports and service request descriptions.
 */
public class KMPAlgorithm {

    public static class KMPResult {
        public final int[] lpsArray;
        public final List<Integer> matchPositions;
        public final int comparisons;
        public final long executionTimeMs;

        public KMPResult(int[] lpsArray, List<Integer> matchPositions, int comparisons, long executionTimeMs) {
            this.lpsArray = lpsArray;
            this.matchPositions = matchPositions;
            this.comparisons = comparisons;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Build the Longest Proper Prefix which is also Suffix (LPS) array.
     */
    public static int[] buildLPS(String pattern) {
        int m = pattern.length();
        int[] lps = new int[m];
        int len = 0;
        int i = 1;
        lps[0] = 0;
        while (i < m) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = 0;
                    i++;
                }
            }
        }
        return lps;
    }

    /**
     * Search for all occurrences of pattern in text using KMP.
     *
     * @param text    the text to search
     * @param pattern the pattern to find
     * @return KMPResult with LPS, positions, and stats
     */
    public static KMPResult search(String text, String pattern) {
        long start = System.currentTimeMillis();

        List<Integer> positions = new ArrayList<>();
        int comparisons = 0;

        if (pattern.isEmpty() || text.isEmpty()) {
            return new KMPResult(new int[0], positions, 0, 0);
        }

        int[] lps = buildLPS(pattern);
        int n = text.length();
        int m = pattern.length();

        int i = 0; // index for text
        int j = 0; // index for pattern

        while (i < n) {
            comparisons++;
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == m) {
                positions.add(i - j);
                j = lps[j - 1];
            } else if (i < n && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0) {
                    j = lps[j - 1];
                } else {
                    i++;
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new KMPResult(lps, positions, comparisons, elapsed);
    }
}
