package com.smartcityx.algorithm;

import com.smartcityx.algorithm.dp.EditDistance;
import com.smartcityx.algorithm.dp.MatrixChainDP;
import com.smartcityx.algorithm.dp.SubsetSumDP;
import com.smartcityx.algorithm.flow.BipartiteMatching;
import com.smartcityx.algorithm.flow.DinicAlgorithm;
import com.smartcityx.algorithm.flow.EdmondsKarp;
import com.smartcityx.algorithm.flow.FlowGraph;
import com.smartcityx.algorithm.np.NPCompleteAlgorithms;
import com.smartcityx.algorithm.randomized.RandomizedAlgorithms;
import com.smartcityx.algorithm.string.AhoCorasick;
import com.smartcityx.algorithm.string.KMPAlgorithm;
import com.smartcityx.algorithm.string.RabinKarpAlgorithm;
import com.smartcityx.algorithm.string.ZAlgorithm;
import com.smartcityx.algorithm.suffix.SuffixArray;
import com.smartcityx.algorithm.suffix.SuffixAutomaton;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AlgorithmTests {

    @Test
    void testStringAlgorithms() {
        String text = "ababcabcabababd";
        String pattern = "ababd";
        
        KMPAlgorithm.KMPResult kmp = KMPAlgorithm.search(text, pattern);
        assertEquals(1, kmp.matchPositions.size());
        assertEquals(10, kmp.matchPositions.get(0));

        ZAlgorithm.ZResult z = ZAlgorithm.search(text, pattern);
        assertEquals(1, z.matchPositions.size());
        assertEquals(10, z.matchPositions.get(0));

        RabinKarpAlgorithm.RabinKarpResult rk = RabinKarpAlgorithm.search(text, pattern);
        assertEquals(1, rk.matchPositions.size());
        assertEquals(10, rk.matchPositions.get(0));

        AhoCorasick.AhoCorasickResult ac = AhoCorasick.searchAll(text, Arrays.asList("ababd", "abc"));
        assertTrue(ac.occurrences.containsKey("ababd"));
        assertTrue(ac.occurrences.containsKey("abc"));
        assertEquals(10, ac.occurrences.get("ababd").get(0));
        assertEquals(2, ac.occurrences.get("abc").get(0));
    }

    @Test
    void testSuffixStructures() {
        String text = "banana";
        SuffixArray.SuffixArrayResult sa = SuffixArray.analyze(text, null);
        
        assertEquals(6, sa.suffixArray.length);
        assertEquals(6, sa.lcpArray.length);
        assertEquals(5, sa.suffixArray[0]); // "a"

        SuffixAutomaton.SAMResult sam = SuffixAutomaton.analyze(text, "nan");
        assertTrue(sam.patternFound);
    }

    @Test
    void testDynamicProgramming() {
        SubsetSumDP.SubsetSumResult ss = SubsetSumDP.solve(new int[]{3, 34, 4, 12, 5, 2}, 9);
        assertTrue(ss.possible);
        assertNotNull(ss.subsetFound);
        
        MatrixChainDP.MatrixChainResult mc = MatrixChainDP.solve(new int[]{1, 2, 3, 4});
        assertEquals(18, mc.minCost); // (1x2) x (2x3) x (3x4)
        
        EditDistance.EditResult lev = EditDistance.levenshtein("kitten", "sitting");
        assertEquals(3, lev.distance);
    }

    @Test
    void testNetworkFlow() {
        FlowGraph g = new FlowGraph(6);
        g.addEdge(0, 1, 16);
        g.addEdge(0, 2, 13);
        g.addEdge(1, 2, 10);
        g.addEdge(1, 3, 12);
        g.addEdge(2, 1, 4);
        g.addEdge(2, 4, 14);
        g.addEdge(3, 2, 9);
        g.addEdge(3, 5, 20);
        g.addEdge(4, 3, 7);
        g.addEdge(4, 5, 4);

        FlowGraph.FlowResult ek = EdmondsKarp.maxFlow(g, 0, 5);
        assertEquals(23, ek.maxFlow);

        // recreate graph for Dinic
        FlowGraph g2 = new FlowGraph(6);
        g2.addEdge(0, 1, 16);
        g2.addEdge(0, 2, 13);
        g2.addEdge(1, 2, 10);
        g2.addEdge(1, 3, 12);
        g2.addEdge(2, 1, 4);
        g2.addEdge(2, 4, 14);
        g2.addEdge(3, 2, 9);
        g2.addEdge(3, 5, 20);
        g2.addEdge(4, 3, 7);
        g2.addEdge(4, 5, 4);

        FlowGraph.FlowResult dinic = DinicAlgorithm.maxFlow(g2, 0, 5);
        assertEquals(23, dinic.maxFlow);
    }

    @Test
    void testBipartiteMatching() {
        List<String> left = Arrays.asList("A1", "A2", "A3");
        List<String> right = Arrays.asList("E1", "E2", "E3");
        List<int[]> edges = Arrays.asList(
            new int[]{0, 0},
            new int[]{0, 1},
            new int[]{1, 0},
            new int[]{2, 2}
        );

        BipartiteMatching.MatchingResult match = BipartiteMatching.solve(left, right, edges);
        assertEquals(3, match.matchingSize);
    }

    @Test
    void testNPComplete() {
        NPCompleteAlgorithms.Graph g = new NPCompleteAlgorithms.Graph(4, Arrays.asList(
            new int[]{0, 1},
            new int[]{1, 2},
            new int[]{2, 3},
            new int[]{3, 0}
        ));

        NPCompleteAlgorithms.VertexCoverResult vc = NPCompleteAlgorithms.vertexCover2Approx(g);
        assertTrue(vc.cover.size() >= 2);

        NPCompleteAlgorithms.CliqueResult cq = NPCompleteAlgorithms.maxClique(g);
        assertEquals(2, cq.size);
    }

    @Test
    void testRandomizedAndParallel() {
        RandomizedAlgorithms.QuickSortResult qs = RandomizedAlgorithms.randomizedQuickSort(new int[]{5, 2, 9, 1, 5, 6});
        assertArrayEquals(new int[]{1, 2, 5, 5, 6, 9}, qs.sorted);

        RandomizedAlgorithms.ReservoirResult rs = RandomizedAlgorithms.reservoirSample(new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 10}, 3);
        assertEquals(3, rs.sample.length);

        RandomizedAlgorithms.MillerRabinResult mr = RandomizedAlgorithms.millerRabin(17, 5);
        assertTrue(mr.isPrime);
    }
}
