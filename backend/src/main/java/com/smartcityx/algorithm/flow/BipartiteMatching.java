package com.smartcityx.algorithm.flow;

import java.util.*;

/**
 * Bipartite Matching using augmenting paths (Hopcroft-Karp simplified).
 * Used in SmartCityX to assign city resources to service requirements.
 * Time: O(E * sqrt(V))
 */
public class BipartiteMatching {

    public static class MatchingResult {
        public final int matchingSize;
        public final Map<Integer, Integer> matching; // left -> right
        public final List<String> matchingPairs;
        public final long executionTimeMs;

        public MatchingResult(int matchingSize, Map<Integer, Integer> matching,
                              List<String> matchingPairs, long executionTimeMs) {
            this.matchingSize = matchingSize;
            this.matching = matching;
            this.matchingPairs = matchingPairs;
            this.executionTimeMs = executionTimeMs;
        }
    }

    private final int left, right;
    private final List<List<Integer>> adj;
    private int[] matchL, matchR;

    public BipartiteMatching(int left, int right) {
        this.left = left;
        this.right = right;
        adj = new ArrayList<>();
        for (int i = 0; i < left; i++) adj.add(new ArrayList<>());
        matchL = new int[left];
        matchR = new int[right];
        Arrays.fill(matchL, -1);
        Arrays.fill(matchR, -1);
    }

    public void addEdge(int u, int v) {
        adj.get(u).add(v);
    }

    private boolean dfs(int u, boolean[] visited) {
        for (int v : adj.get(u)) {
            if (!visited[v]) {
                visited[v] = true;
                if (matchR[v] == -1 || dfs(matchR[v], visited)) {
                    matchL[u] = v;
                    matchR[v] = u;
                    return true;
                }
            }
        }
        return false;
    }

    public int maxMatching() {
        int result = 0;
        for (int u = 0; u < left; u++) {
            if (matchL[u] == -1) {
                boolean[] visited = new boolean[right];
                if (dfs(u, visited)) result++;
            }
        }
        return result;
    }

    /**
     * Static helper: solve bipartite matching given named sets.
     */
    public static MatchingResult solve(List<String> leftNodes, List<String> rightNodes,
                                       List<int[]> edges) {
        long start = System.currentTimeMillis();

        BipartiteMatching bm = new BipartiteMatching(leftNodes.size(), rightNodes.size());
        for (int[] e : edges) bm.addEdge(e[0], e[1]);

        int size = bm.maxMatching();

        Map<Integer, Integer> matchMap = new LinkedHashMap<>();
        List<String> pairs = new ArrayList<>();
        for (int i = 0; i < leftNodes.size(); i++) {
            if (bm.matchL[i] != -1) {
                matchMap.put(i, bm.matchL[i]);
                pairs.add(leftNodes.get(i) + " → " + rightNodes.get(bm.matchL[i]));
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new MatchingResult(size, matchMap, pairs, elapsed);
    }
}
