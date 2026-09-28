package com.smartcityx.algorithm.randomized;

import java.math.BigInteger;
import java.util.*;

/**
 * M6 Randomized and Parallel Algorithm implementations.
 * - Randomized QuickSort
 * - Reservoir Sampling
 * - Miller-Rabin Primality Test
 * - Blelloch Prefix Scan (simulated)
 * - Parallel Reduce (simulated)
 */
public class RandomizedAlgorithms {

    // ────────────────────────────────────────────
    //  Randomized QuickSort
    // ────────────────────────────────────────────

    public static class QuickSortResult {
        public final int[] sorted;
        public final int comparisons;
        public final long executionTimeMs;

        public QuickSortResult(int[] sorted, int comparisons, long executionTimeMs) {
            this.sorted = sorted;
            this.comparisons = comparisons;
            this.executionTimeMs = executionTimeMs;
        }
    }

    private static int comparisons;
    private static final Random rnd = new Random();

    public static QuickSortResult randomizedQuickSort(int[] arr) {
        long start = System.currentTimeMillis();
        comparisons = 0;
        int[] copy = Arrays.copyOf(arr, arr.length);
        quickSort(copy, 0, copy.length - 1);
        long elapsed = System.currentTimeMillis() - start;
        return new QuickSortResult(copy, comparisons, elapsed);
    }

    private static void quickSort(int[] arr, int lo, int hi) {
        if (lo < hi) {
            int pivotIdx = lo + rnd.nextInt(hi - lo + 1);
            int tmp = arr[pivotIdx]; arr[pivotIdx] = arr[hi]; arr[hi] = tmp;
            int pi = partition(arr, lo, hi);
            quickSort(arr, lo, pi - 1);
            quickSort(arr, pi + 1, hi);
        }
    }

    private static int partition(int[] arr, int lo, int hi) {
        int pivot = arr[hi];
        int i = lo - 1;
        for (int j = lo; j < hi; j++) {
            comparisons++;
            if (arr[j] <= pivot) {
                i++;
                int tmp = arr[i]; arr[i] = arr[j]; arr[j] = tmp;
            }
        }
        int tmp = arr[i + 1]; arr[i + 1] = arr[hi]; arr[hi] = tmp;
        return i + 1;
    }

    // ────────────────────────────────────────────
    //  Reservoir Sampling
    // ────────────────────────────────────────────

    public static class ReservoirResult {
        public final int[] sample;
        public final long executionTimeMs;

        public ReservoirResult(int[] sample, long executionTimeMs) {
            this.sample = sample;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static ReservoirResult reservoirSample(int[] stream, int k) {
        long start = System.currentTimeMillis();

        int[] reservoir = Arrays.copyOf(stream, Math.min(k, stream.length));

        for (int i = k; i < stream.length; i++) {
            int j = rnd.nextInt(i + 1);
            if (j < k) reservoir[j] = stream[i];
        }

        long elapsed = System.currentTimeMillis() - start;
        return new ReservoirResult(reservoir, elapsed);
    }

    // ────────────────────────────────────────────
    //  Miller-Rabin Primality Test
    // ────────────────────────────────────────────

    public static class MillerRabinResult {
        public final boolean isPrime;
        public final int rounds;
        public final long executionTimeMs;

        public MillerRabinResult(boolean isPrime, int rounds, long executionTimeMs) {
            this.isPrime = isPrime;
            this.rounds = rounds;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static MillerRabinResult millerRabin(long n, int rounds) {
        long start = System.currentTimeMillis();

        boolean prime = false;
        if (n < 2) {
            prime = false;
        } else if (n == 2 || n == 3) {
            prime = true;
        } else if (n % 2 == 0) {
            prime = false;
        } else {
            prime = isProbablePrime(n, rounds);
        }

        long elapsed = System.currentTimeMillis() - start;
        return new MillerRabinResult(prime, rounds, elapsed);
    }

    private static boolean isProbablePrime(long n, int k) {
        long d = n - 1;
        int r = 0;
        while (d % 2 == 0) { d /= 2; r++; }

        for (int i = 0; i < k; i++) {
            long a = 2 + (long)(rnd.nextDouble() * (n - 4));
            long x = modPow(a, d, n);
            if (x == 1 || x == n - 1) continue;
            boolean composite = true;
            for (int j = 0; j < r - 1; j++) {
                x = mulMod(x, x, n);
                if (x == n - 1) { composite = false; break; }
            }
            if (composite) return false;
        }
        return true;
    }

    private static long modPow(long base, long exp, long mod) {
        long result = 1;
        base %= mod;
        while (exp > 0) {
            if ((exp & 1) == 1) result = mulMod(result, base, mod);
            base = mulMod(base, base, mod);
            exp >>= 1;
        }
        return result;
    }

    private static long mulMod(long a, long b, long mod) {
        return BigInteger.valueOf(a).multiply(BigInteger.valueOf(b)).mod(BigInteger.valueOf(mod)).longValue();
    }

    // ────────────────────────────────────────────
    //  Blelloch Prefix Scan (simulated sequential)
    // ────────────────────────────────────────────

    public static class BlellochResult {
        public final int[] prefixSums;
        public final int[][] upTree;   // up-sweep tree levels
        public final int[][] downTree; // down-sweep tree levels
        public final int totalWork;    // O(n)
        public final int span;         // O(log n)
        public final long executionTimeMs;

        public BlellochResult(int[] prefixSums, int[][] upTree, int[][] downTree,
                              int totalWork, int span, long executionTimeMs) {
            this.prefixSums = prefixSums;
            this.upTree = upTree;
            this.downTree = downTree;
            this.totalWork = totalWork;
            this.span = span;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static BlellochResult blellochScan(int[] arr) {
        long start = System.currentTimeMillis();

        int n = arr.length;
        // Pad to power of 2
        int size = 1;
        while (size < n) size <<= 1;

        int[] a = Arrays.copyOf(arr, size);
        List<int[]> upLevels = new ArrayList<>();
        List<int[]> downLevels = new ArrayList<>();

        // Up-sweep (reduce)
        int stride = 1;
        while (stride < size) {
            int[] level = Arrays.copyOf(a, size);
            for (int i = stride * 2 - 1; i < size; i += stride * 2) {
                a[i] += a[i - stride];
            }
            upLevels.add(Arrays.copyOf(a, Math.min(size, 16)));
            stride <<= 1;
        }

        // Set root to identity
        a[size - 1] = 0;

        // Down-sweep
        stride = size >> 1;
        while (stride > 0) {
            for (int i = stride * 2 - 1; i < size; i += stride * 2) {
                int t = a[i - stride];
                a[i - stride] = a[i];
                a[i] = a[i] + t;
            }
            downLevels.add(Arrays.copyOf(a, Math.min(size, 16)));
            stride >>= 1;
        }

        int[] result = Arrays.copyOf(a, n);
        // Fix: make exclusive → inclusive
        for (int i = 0; i < n; i++) result[i] += arr[i];

        int span = (int)(Math.log(n) / Math.log(2)) + 1;

        long elapsed = System.currentTimeMillis() - start;
        return new BlellochResult(result, upLevels.toArray(new int[0][]),
                downLevels.toArray(new int[0][]), 2 * n, span, elapsed);
    }

    // ────────────────────────────────────────────
    //  Parallel Reduce (simulated)
    // ────────────────────────────────────────────

    public static class ParallelReduceResult {
        public final int total;
        public final int[][] levels;
        public final int work;  // O(n)
        public final int span;  // O(log n)
        public final long executionTimeMs;

        public ParallelReduceResult(int total, int[][] levels, int work, int span, long executionTimeMs) {
            this.total = total;
            this.levels = levels;
            this.work = work;
            this.span = span;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static ParallelReduceResult parallelReduce(int[] arr) {
        long start = System.currentTimeMillis();

        int n = arr.length;
        List<int[]> levels = new ArrayList<>();
        int[] cur = Arrays.copyOf(arr, n);
        levels.add(Arrays.copyOf(cur, Math.min(n, 16)));

        while (cur.length > 1) {
            int newLen = (cur.length + 1) / 2;
            int[] next = new int[newLen];
            for (int i = 0; i < cur.length - 1; i += 2) next[i / 2] = cur[i] + cur[i + 1];
            if (cur.length % 2 == 1) next[newLen - 1] = cur[cur.length - 1];
            cur = next;
            levels.add(Arrays.copyOf(cur, Math.min(cur.length, 16)));
        }

        int total = cur[0];
        int span = (int)(Math.log(n) / Math.log(2)) + 1;

        long elapsed = System.currentTimeMillis() - start;
        return new ParallelReduceResult(total, levels.toArray(new int[0][]), n - 1, span, elapsed);
    }
}
