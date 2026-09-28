package com.smartcityx.algorithm.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Z-Function string algorithm.
 * Z[i] = length of the longest substring starting from s[i] which is also a prefix of s.
 * Time: O(n + m), Space: O(n + m)
 * Used in SmartCityX to identify repeated patterns in city reports.
 */
public class ZAlgorithm {

    public static class ZResult {
        public final int[] zArray;
        public final List<Integer> matchPositions;
        public final long executionTimeMs;

        public ZResult(int[] zArray, List<Integer> matchPositions, long executionTimeMs) {
            this.zArray = zArray;
            this.matchPositions = matchPositions;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Compute the Z-array for a given string.
     */
    public static int[] buildZArray(String s) {
        int n = s.length();
        int[] z = new int[n];
        z[0] = n;
        int l = 0, r = 0;

        for (int i = 1; i < n; i++) {
            if (i < r) {
                z[i] = Math.min(r - i, z[i - l]);
            }
            while (i + z[i] < n && s.charAt(z[i]) == s.charAt(i + z[i])) {
                z[i]++;
            }
            if (i + z[i] > r) {
                l = i;
                r = i + z[i];
            }
        }
        return z;
    }

    /**
     * Search for all occurrences of pattern in text using Z-algorithm.
     */
    public static ZResult search(String text, String pattern) {
        long start = System.currentTimeMillis();

        List<Integer> positions = new ArrayList<>();

        if (pattern.isEmpty() || text.isEmpty()) {
            return new ZResult(new int[0], positions, 0);
        }

        String concat = pattern + "$" + text;
        int[] z = buildZArray(concat);
        int m = pattern.length();

        for (int i = m + 1; i < concat.length(); i++) {
            if (z[i] == m) {
                positions.add(i - m - 1);
            }
        }

        // Return only the text portion of the Z array for display
        int[] textZ = new int[text.length()];
        for (int i = 0; i < text.length(); i++) {
            textZ[i] = z[m + 1 + i];
        }

        long elapsed = System.currentTimeMillis() - start;
        return new ZResult(textZ, positions, elapsed);
    }
}
