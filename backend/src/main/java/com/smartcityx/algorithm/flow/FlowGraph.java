package com.smartcityx.algorithm.flow;

import java.util.*;

/**
 * Max-Flow graph structure shared by Ford-Fulkerson, Edmonds-Karp, and Dinic.
 */
public class FlowGraph {
    public final int n;
    public final List<int[]> edges; // [from, to, capacity, flow]
    public final List<List<Integer>> graph;

    public FlowGraph(int n) {
        this.n = n;
        this.edges = new ArrayList<>();
        this.graph = new ArrayList<>();
        for (int i = 0; i < n; i++) graph.add(new ArrayList<>());
    }

    /** Add directed edge with capacity. Returns edge index. */
    public int addEdge(int from, int to, int capacity) {
        graph.get(from).add(edges.size());
        edges.add(new int[]{from, to, capacity, 0});
        graph.get(to).add(edges.size());
        edges.add(new int[]{to, from, 0, 0}); // reverse edge
        return edges.size() - 2;
    }

    /** Get remaining capacity of edge idx. */
    public int residual(int idx) {
        int[] e = edges.get(idx);
        return e[2] - e[3];
    }

    /** Push flow through edge idx. */
    public void pushFlow(int idx, int f) {
        edges.get(idx)[3] += f;
        edges.get(idx ^ 1)[3] -= f;
    }

    public static class FlowResult {
        public final int maxFlow;
        public final List<int[]> edgeFlows; // [from, to, capacity, flow]
        public final Set<Integer> minCutSource; // nodes on source side of min-cut
        public final long executionTimeMs;
        public final String algorithm;

        public FlowResult(int maxFlow, List<int[]> edgeFlows, Set<Integer> minCutSource,
                          long executionTimeMs, String algorithm) {
            this.maxFlow = maxFlow;
            this.edgeFlows = edgeFlows;
            this.minCutSource = minCutSource;
            this.executionTimeMs = executionTimeMs;
            this.algorithm = algorithm;
        }
    }
}
