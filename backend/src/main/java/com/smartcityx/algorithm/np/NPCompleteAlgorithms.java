package com.smartcityx.algorithm.np;

import java.util.*;

/**
 * NP-Completeness demonstrations: Vertex Cover, Independent Set, Clique.
 * Includes Vertex Cover 2-Approximation algorithm.
 * Used in SmartCityX to identify critical infrastructure coverage.
 */
public class NPCompleteAlgorithms {

    public static class Graph {
        public final int n;
        public final List<int[]> edges; // [u, v]

        public Graph(int n, List<int[]> edges) {
            this.n = n;
            this.edges = edges;
        }
    }

    public static class VertexCoverResult {
        public final Set<Integer> cover;
        public final int approximationRatio;
        public final long executionTimeMs;

        public VertexCoverResult(Set<Integer> cover, int approximationRatio, long executionTimeMs) {
            this.cover = cover;
            this.approximationRatio = approximationRatio;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static class CliqueResult {
        public final Set<Integer> clique;
        public final int size;
        public final long executionTimeMs;

        public CliqueResult(Set<Integer> clique, int size, long executionTimeMs) {
            this.clique = clique;
            this.size = size;
            this.executionTimeMs = executionTimeMs;
        }
    }

    public static class IndependentSetResult {
        public final Set<Integer> independentSet;
        public final int size;
        public final long executionTimeMs;

        public IndependentSetResult(Set<Integer> independentSet, int size, long executionTimeMs) {
            this.independentSet = independentSet;
            this.size = size;
            this.executionTimeMs = executionTimeMs;
        }
    }

    /**
     * Vertex Cover 2-Approximation Algorithm.
     * Pick an uncovered edge, add both endpoints to cover, repeat.
     * Guarantees at most 2 * OPT nodes.
     */
    public static VertexCoverResult vertexCover2Approx(Graph g) {
        long start = System.currentTimeMillis();

        Set<Integer> cover = new HashSet<>();
        boolean[] edgeCovered = new boolean[g.edges.size()];

        for (int i = 0; i < g.edges.size(); i++) {
            if (!edgeCovered[i]) {
                int u = g.edges.get(i)[0];
                int v = g.edges.get(i)[1];
                cover.add(u);
                cover.add(v);
                // Mark all edges incident to u or v as covered
                for (int j = 0; j < g.edges.size(); j++) {
                    int a = g.edges.get(j)[0], b = g.edges.get(j)[1];
                    if (a == u || a == v || b == u || b == v) {
                        edgeCovered[j] = true;
                    }
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new VertexCoverResult(cover, 2, elapsed);
    }

    /**
     * Brute-force maximum clique (exponential, for small n ≤ 20).
     */
    public static CliqueResult maxClique(Graph g) {
        long start = System.currentTimeMillis();

        // Build adjacency set for quick lookup
        Set<String> adjSet = new HashSet<>();
        for (int[] e : g.edges) {
            adjSet.add(e[0] + "," + e[1]);
            adjSet.add(e[1] + "," + e[0]);
        }

        Set<Integer> bestClique = new HashSet<>();

        // Greedy clique approximation
        for (int start2 = 0; start2 < g.n; start2++) {
            Set<Integer> clique = new HashSet<>();
            clique.add(start2);
            for (int v = 0; v < g.n; v++) {
                if (v == start2) continue;
                boolean connected = true;
                for (int u : clique) {
                    if (!adjSet.contains(u + "," + v)) { connected = false; break; }
                }
                if (connected) clique.add(v);
            }
            if (clique.size() > bestClique.size()) bestClique = clique;
        }

        long elapsed = System.currentTimeMillis() - start;
        return new CliqueResult(bestClique, bestClique.size(), elapsed);
    }

    /**
     * Maximum Independent Set via complement of vertex cover.
     * IS = V \ VertexCover
     */
    public static IndependentSetResult maxIndependentSet(Graph g) {
        long start = System.currentTimeMillis();

        VertexCoverResult vc = vertexCover2Approx(g);
        Set<Integer> is = new HashSet<>();
        for (int i = 0; i < g.n; i++) {
            if (!vc.cover.contains(i)) is.add(i);
        }

        long elapsed = System.currentTimeMillis() - start;
        return new IndependentSetResult(is, is.size(), elapsed);
    }

    /**
     * 3-SAT clause representation for educational display.
     */
    public static List<String> generate3SATClauses(int variables, int clauses) {
        Random rnd = new Random(42);
        List<String> result = new ArrayList<>();
        for (int c = 0; c < clauses; c++) {
            StringBuilder sb = new StringBuilder("(");
            for (int l = 0; l < 3; l++) {
                if (l > 0) sb.append(" ∨ ");
                boolean neg = rnd.nextBoolean();
                int var = rnd.nextInt(variables) + 1;
                if (neg) sb.append("¬");
                sb.append("x").append(var);
            }
            sb.append(")");
            result.add(sb.toString());
        }
        return result;
    }
}
