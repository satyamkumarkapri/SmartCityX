package com.smartcityx.algorithm.suffix;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Suffix Array construction using O(n log n) prefix-doubling algorithm.
 * Combined with Kasai's algorithm for LCP array.
 * Used in SmartCityX to index city documents for substring search.
 */
public class SuffixArray {

    public static class SuffixArrayResult {
        public final int[] suffixArray;
        public final int[] lcpArray;
        public final List<String> suffixes;
        public final List<Integer> matchPositions;
        public final long executionTimeMs;

        public SuffixArrayResult(int[] suffixArray, int[] lcpArray, List<String> suffixes,
                                 List<Integer> matchPositions, long executionTimeMs) {
            this.suffixArray = suffixArray;
            this.lcpArray = lcpArray;
            this.suffixes = suffixes;
            this.matchPositions = matchPositions;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Build suffix array using prefix-doubling (O(n log^2 n) with Arrays.sort).
     */
    public static int[] buildSuffixArray(String s) {
        int n = s.length();
        Integer[] sa = new Integer[n];
        int[] rank = new int[n];
        int[] tmp = new int[n];

        for (int i = 0; i < n; i++) {
            sa[i] = i;
            rank[i] = s.charAt(i);
        }

        for (int gap = 1; gap < n; gap <<= 1) {
            final int[] r = rank.clone();
            final int g = gap;

            Arrays.sort(sa, (a, b) -> {
                if (r[a] != r[b]) return r[a] - r[b];
                int ra = a + g < n ? r[a + g] : -1;
                int rb = b + g < n ? r[b + g] : -1;
                return ra - rb;
            });

            tmp[sa[0]] = 0;
            for (int i = 1; i < n; i++) {
                int prev = sa[i - 1], cur = sa[i];
                boolean same = r[prev] == r[cur];
                if (same) {
                    int rp = prev + g < n ? r[prev + g] : -1;
                    int rc = cur + g < n ? r[cur + g] : -1;
                    same = (rp == rc);
                }
                tmp[cur] = tmp[prev] + (same ? 0 : 1);
            }
            System.arraycopy(tmp, 0, rank, 0, n);
        }

        int[] result = new int[n];
        for (int i = 0; i < n; i++) result[i] = sa[i];
        return result;
    }

    /**
     * Kasai's algorithm to compute LCP array in O(n).
     */
    public static int[] buildLCP(String s, int[] sa) {
        int n = s.length();
        int[] rank = new int[n];
        int[] lcp = new int[n];

        for (int i = 0; i < n; i++) rank[sa[i]] = i;

        int h = 0;
        for (int i = 0; i < n; i++) {
            if (rank[i] > 0) {
                int j = sa[rank[i] - 1];
                while (i + h < n && j + h < n && s.charAt(i + h) == s.charAt(j + h)) h++;
                lcp[rank[i]] = h;
                if (h > 0) h--;
            }
        }
        return lcp;
    }

    /**
     * Binary search in suffix array to find all occurrences of a pattern.
     */
    public static List<Integer> search(String text, int[] sa, String pattern) {
        List<Integer> positions = new ArrayList<>();
        int n = text.length();
        int m = pattern.length();

        // Find left boundary
        int lo = 0, hi = n;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            String suf = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suf.compareTo(pattern) < 0) lo = mid + 1;
            else hi = mid;
        }
        int left = lo;

        // Find right boundary
        hi = n;
        while (lo < hi) {
            int mid = (lo + hi) / 2;
            String suf = text.substring(sa[mid], Math.min(sa[mid] + m, n));
            if (suf.compareTo(pattern) <= 0 && suf.startsWith(pattern)) lo = mid + 1;
            else if (suf.compareTo(pattern) < 0) lo = mid + 1;
            else hi = mid;
        }
        int right = lo;

        for (int i = left; i < right; i++) positions.add(sa[i]);
        return positions;
    }

    /**
     * Full analysis: build SA, LCP, and optionally search for a pattern.
     */
    public static SuffixArrayResult analyze(String text, String pattern) {
        long start = System.currentTimeMillis();

        int[] sa = buildSuffixArray(text);
        int[] lcp = buildLCP(text, sa);

        List<String> suffixes = new ArrayList<>();
        int display = Math.min(sa.length, 20);
        for (int i = 0; i < display; i++) {
            String suf = text.substring(sa[i]);
            suffixes.add(suf.length() > 40 ? suf.substring(0, 40) + "…" : suf);
        }

        List<Integer> positions = new ArrayList<>();
        if (pattern != null && !pattern.isEmpty()) {
            positions = search(text, sa, pattern);
        }

        long elapsed = System.currentTimeMillis() - start;
        return new SuffixArrayResult(sa, lcp, suffixes, positions, elapsed);
    }
}
