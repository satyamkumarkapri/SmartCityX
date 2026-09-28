package com.smartcityx.algorithm.flow;

import java.util.*;

/**
 * Dinic's algorithm for maximum flow.
 * Time: O(V^2 * E), much faster in practice.
 * Used in SmartCityX for larger city-flow networks.
 */
public class DinicAlgorithm {

    private static boolean bfs(FlowGraph g, int s, int t, int[] level) {
        Arrays.fill(level, -1);
        level[s] = 0;
        Queue<Integer> q = new LinkedList<>();
        q.add(s);

        while (!q.isEmpty()) {
            int u = q.poll();
            for (int idx : g.graph.get(u)) {
                int[] e = g.edges.get(idx);
                int v = e[1];
                if (level[v] < 0 && g.residual(idx) > 0) {
                    level[v] = level[u] + 1;
                    q.add(v);
                }
            }
        }
        return level[t] >= 0;
    }

    private static int dfs(FlowGraph g, int u, int t, int pushed, int[] level, int[] iter) {
        if (u == t) return pushed;
        for (; iter[u] < g.graph.get(u).size(); iter[u]++) {
            int idx = g.graph.get(u).get(iter[u]);
            int[] e = g.edges.get(idx);
            int v = e[1];
            if (level[v] != level[u] + 1 || g.residual(idx) <= 0) continue;
            int d = dfs(g, v, t, Math.min(pushed, g.residual(idx)), level, iter);
            if (d > 0) {
                g.pushFlow(idx, d);
                return d;
            }
        }
        return 0;
    }

    public static FlowGraph.FlowResult maxFlow(FlowGraph g, int source, int sink) {
        long start = System.currentTimeMillis();

        int[] level = new int[g.n];
        int totalFlow = 0;

        while (bfs(g, source, sink, level)) {
            int[] iter = new int[g.n];
            int pushed;
            while ((pushed = dfs(g, source, sink, Integer.MAX_VALUE, level, iter)) > 0) {
                totalFlow += pushed;
            }
        }

        // Min-cut
        Set<Integer> sourceSet = new HashSet<>();
        for (int i = 0; i < g.n; i++) {
            if (level[i] >= 0) sourceSet.add(i);
        }

        long elapsed = System.currentTimeMillis() - start;
        return new FlowGraph.FlowResult(totalFlow, g.edges, sourceSet, elapsed, "Dinic");
    }
}
