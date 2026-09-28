package com.smartcityx.algorithm.string;

import java.util.*;

/**
 * Aho-Corasick multi-pattern string matching automaton.
 * Time: O(n + m + k), Space: O(ALPHA * m)
 * Used in SmartCityX to detect multiple emergency and service keywords simultaneously.
 */
public class AhoCorasick {

    private static final int ALPHA = 256;

    public static class AhoCorasickResult {
        public final Map<String, List<Integer>> occurrences;
        public final int trieNodeCount;
        public final long executionTimeMs;

        public AhoCorasickResult(Map<String, List<Integer>> occurrences, int trieNodeCount, long executionTimeMs) {
            this.occurrences = occurrences;
            this.trieNodeCount = trieNodeCount;
            this.executionTimeMs = executionTimeMs;
        }
    }

    private final int[][] go;
    private final int[] fail;
    private final List<List<String>> output;
    private int size;

    public AhoCorasick(int maxNodes) {
        go = new int[maxNodes][ALPHA];
        fail = new int[maxNodes];
        output = new ArrayList<>();
        for (int i = 0; i < maxNodes; i++) {
            Arrays.fill(go[i], -1);
            output.add(new ArrayList<>());
        }
        size = 0;
        // Root node
        size++;
    }

    /** Insert a keyword into the trie. */
    public void addPattern(String pattern) {
        int cur = 0;
        for (char c : pattern.toCharArray()) {
            int ch = c;
            if (go[cur][ch] == -1) {
                Arrays.fill(go[size], -1);
                output.add(new ArrayList<>());
                go[cur][ch] = size++;
            }
            cur = go[cur][ch];
        }
        output.get(cur).add(pattern);
    }

    /** Build failure links using BFS. */
    public void build() {
        Queue<Integer> queue = new LinkedList<>();
        for (int c = 0; c < ALPHA; c++) {
            if (go[0][c] == -1) {
                go[0][c] = 0;
            } else {
                fail[go[0][c]] = 0;
                queue.add(go[0][c]);
            }
        }
        while (!queue.isEmpty()) {
            int u = queue.poll();
            output.get(u).addAll(output.get(fail[u]));
            for (int c = 0; c < ALPHA; c++) {
                if (go[u][c] == -1) {
                    go[u][c] = go[fail[u]][c];
                } else {
                    fail[go[u][c]] = go[fail[u]][c];
                    queue.add(go[u][c]);
                }
            }
        }
    }

    /**
     * Search text for all keywords and return positions per keyword.
     */
    public Map<String, List<Integer>> search(String text) {
        Map<String, List<Integer>> result = new HashMap<>();
        int cur = 0;
        for (int i = 0; i < text.length(); i++) {
            cur = go[cur][text.charAt(i)];
            for (String word : output.get(cur)) {
                result.computeIfAbsent(word, k -> new ArrayList<>()).add(i - word.length() + 1);
            }
        }
        return result;
    }

    /**
     * Static convenience method for single-call usage from the service layer.
     */
    public static AhoCorasickResult searchAll(String text, List<String> keywords) {
        long start = System.currentTimeMillis();

        int maxNodes = keywords.stream().mapToInt(String::length).sum() + 10;
        AhoCorasick ac = new AhoCorasick(maxNodes);
        for (String kw : keywords) ac.addPattern(kw);
        ac.build();

        Map<String, List<Integer>> occurrences = ac.search(text);

        long elapsed = System.currentTimeMillis() - start;
        return new AhoCorasickResult(occurrences, ac.size, elapsed);
    }
}
