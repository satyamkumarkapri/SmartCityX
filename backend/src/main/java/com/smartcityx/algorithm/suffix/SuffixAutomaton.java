package com.smartcityx.algorithm.suffix;

import java.util.*;

/**
 * Suffix Automaton (SAM) – smallest DFA accepting all suffixes of a string.
 * Time & Space: O(n)
 * Used in SmartCityX for efficient substring queries on city documents.
 */
public class SuffixAutomaton {

    public static class State {
        public int len;
        public int link;
        public Map<Character, Integer> next = new HashMap<>();

        public State(int len, int link) {
            this.len = len;
            this.link = link;
        }
    }

    public static class SAMResult {
        public final int stateCount;
        public final boolean patternFound;
        public final long executionTimeMs;
        public final List<String> distinctSubstrings;

        public SAMResult(int stateCount, boolean patternFound, long executionTimeMs, List<String> distinctSubstrings) {
            this.stateCount = stateCount;
            this.patternFound = patternFound;
            this.executionTimeMs = executionTimeMs;
            this.distinctSubstrings = distinctSubstrings;
        }
    }

    private final List<State> states = new ArrayList<>();
    private int last;

    public SuffixAutomaton() {
        states.add(new State(0, -1)); // initial state
        last = 0;
    }

    /** Extend SAM with one character. */
    public void extend(char c) {
        int cur = states.size();
        states.add(new State(states.get(last).len + 1, -1));
        int p = last;

        while (p != -1 && !states.get(p).next.containsKey(c)) {
            states.get(p).next.put(c, cur);
            p = states.get(p).link;
        }

        if (p == -1) {
            states.get(cur).link = 0;
        } else {
            int q = states.get(p).next.get(c);
            if (states.get(p).len + 1 == states.get(q).len) {
                states.get(cur).link = q;
            } else {
                int clone = states.size();
                states.add(new State(states.get(p).len + 1, states.get(q).link));
                states.get(clone).next.putAll(states.get(q).next);
                while (p != -1 && states.get(p).next.getOrDefault(c, -1) == q) {
                    states.get(p).next.put(c, clone);
                    p = states.get(p).link;
                }
                states.get(q).link = clone;
                states.get(cur).link = clone;
            }
        }
        last = cur;
    }

    /** Check if the pattern is a substring accepted by the SAM. */
    public boolean contains(String pattern) {
        int cur = 0;
        for (char c : pattern.toCharArray()) {
            if (!states.get(cur).next.containsKey(c)) return false;
            cur = states.get(cur).next.get(c);
        }
        return true;
    }

    /**
     * Build SAM for text and search for pattern.
     */
    public static SAMResult analyze(String text, String pattern) {
        long start = System.currentTimeMillis();

        SuffixAutomaton sam = new SuffixAutomaton();
        for (char c : text.toCharArray()) sam.extend(c);

        boolean found = pattern != null && !pattern.isEmpty() && sam.contains(pattern);

        // Collect some distinct substrings for display (up to 15)
        List<String> subs = new ArrayList<>();
        collectSubstrings(sam, text, subs, 15);

        long elapsed = System.currentTimeMillis() - start;
        return new SAMResult(sam.states.size(), found, elapsed, subs);
    }

    private static void collectSubstrings(SuffixAutomaton sam, String text, List<String> result, int limit) {
        // Simple sampling of substrings from text of varying lengths
        Set<String> seen = new LinkedHashSet<>();
        int n = text.length();
        for (int len = 1; len <= Math.min(n, 8) && seen.size() < limit; len++) {
            for (int i = 0; i <= n - len && seen.size() < limit; i++) {
                seen.add(text.substring(i, i + len));
            }
        }
        result.addAll(seen);
    }
}
