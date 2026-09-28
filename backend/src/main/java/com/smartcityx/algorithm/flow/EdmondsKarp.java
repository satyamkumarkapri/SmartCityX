package com.smartcityx.algorithm.flow;

import java.util.*;

/**
 * Edmonds-Karp algorithm (BFS-based Ford-Fulkerson).
 * Time: O(VE^2), Space: O(V+E)
 * Used in SmartCityX for city-network capacity analysis.
 */
public class EdmondsKarp {

    private static int bfs(FlowGraph g, int s, int t, int[] parent) {
        Arrays.fill(parent, -1);
        parent[s] = s;
        Queue<int[]> queue = new LinkedList<>();
        queue.add(new int[]{s, Integer.MAX_VALUE});

        while (!queue.isEmpty()) {
            int[] cur = queue.poll();
            int u = cur[0], flow = cur[1];

            for (int idx : g.graph.get(u)) {
                int[] e = g.edges.get(idx);
                int v = e[1];
                if (parent[v] == -1 && g.residual(idx) > 0) {
                    parent[v] = idx;
                    int newFlow = Math.min(flow, g.residual(idx));
                    if (v == t) return newFlow;
                    queue.add(new int[]{v, newFlow});
                }
            }
        }
        return 0;
    }

    public static FlowGraph.FlowResult maxFlow(FlowGraph g, int source, int sink) {
        long start = System.currentTimeMillis();

        int[] parent = new int[g.n];
        int totalFlow = 0;
        int pushed;

        while ((pushed = bfs(g, source, sink, parent)) > 0) {
            totalFlow += pushed;
            int cur = sink;
            while (cur != source) {
                int idx = parent[cur];
                g.pushFlow(idx, pushed);
                cur = g.edges.get(idx)[0];
            }
        }

        // Find min-cut (nodes reachable from source in residual)
        Set<Integer> sourceSet = new HashSet<>();
        boolean[] visited = new boolean[g.n];
        Queue<Integer> q = new LinkedList<>();
        q.add(source);
        visited[source] = true;
        while (!q.isEmpty()) {
            int u = q.poll();
            sourceSet.add(u);
            for (int idx : g.graph.get(u)) {
                int[] e = g.edges.get(idx);
                if (!visited[e[1]] && g.residual(idx) > 0) {
                    visited[e[1]] = true;
                    q.add(e[1]);
                }
            }
        }

        long elapsed = System.currentTimeMillis() - start;
        return new FlowGraph.FlowResult(totalFlow, g.edges, sourceSet, elapsed, "Edmonds-Karp");
    }
}
