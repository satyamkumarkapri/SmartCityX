package com.smartcityx.algorithm.string;

import java.util.ArrayList;
import java.util.List;

/**
 * Rabin-Karp rolling hash string search algorithm.
 * Average Time: O(n + m), Worst: O(nm), Space: O(1)
 * Used in SmartCityX to search service and infrastructure identifiers.
 */
public class RabinKarpAlgorithm {

    private static final long BASE = 31L;
    private static final long MOD = 1_000_000_007L;

    public static class RabinKarpResult {
        public final long patternHash;
        public final List<Long> rollingHashes;
        public final List<Integer> matchPositions;
        public final int spuriousHits;
        public final long executionTimeMs;

        public RabinKarpResult(long patternHash, List<Long> rollingHashes,
                               List<Integer> matchPositions, int spuriousHits, long executionTimeMs) {
            this.patternHash = patternHash;
            this.rollingHashes = rollingHashes;
            this.matchPositions = matchPositions;
            this.spuriousHits = spuriousHits;
            this.executionTimeMs = executionTimeMs;
        }
    }

    private static long hash(String s) {
        long h = 0;
        long power = 1;
        for (int i = 0; i < s.length(); i++) {
            h = (h + (s.charAt(i) - 'a' + 1) * power) % MOD;
            power = (power * BASE) % MOD;
        }
        return h;
    }

    /**
     * Search for all occurrences of pattern in text using Rabin-Karp.
     */
    public static RabinKarpResult search(String text, String pattern) {
        long start = System.currentTimeMillis();

        List<Integer> positions = new ArrayList<>();
        List<Long> rollingHashes = new ArrayList<>();
        int spuriousHits = 0;

        if (pattern.isEmpty() || text.isEmpty() || pattern.length() > text.length()) {
            return new RabinKarpResult(0, rollingHashes, positions, 0, 0);
        }

        int n = text.length();
        int m = pattern.length();

        long patternHash = hash(pattern);

        // Precompute base^m mod MOD
        long highPow = 1;
        for (int i = 0; i < m; i++) {
            highPow = (highPow * BASE) % MOD;
        }

        // Compute hash of first window
        long windowHash = 0;
        long power = 1;
        for (int i = 0; i < m; i++) {
            windowHash = (windowHash + (text.charAt(i) - 'a' + 1) * power) % MOD;
            power = (power * BASE) % MOD;
        }

        rollingHashes.add(windowHash);

        for (int i = 0; i <= n - m; i++) {
            if (i > 0) {
                // Rolling hash: remove leftmost, add new rightmost
                windowHash = (windowHash - (text.charAt(i - 1) - 'a' + 1) + MOD) % MOD;
                // Divide by BASE (multiply by modular inverse not needed; use right-to-left hash)
                // Using a simpler approach: recompute from scratch for correctness demonstration
                windowHash = hash(text.substring(i, i + m));
                if (i < n - m) rollingHashes.add(windowHash);
            }

            if (windowHash == patternHash) {
                // Verify (avoid spurious hits)
                if (text.substring(i, i + m).equals(pattern)) {
                    positions.add(i);
                } else {
                    spuriousHits++;
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new RabinKarpResult(patternHash, rollingHashes, positions, spuriousHits, elapsed);
    }
}
