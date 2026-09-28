package com.smartcityx.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartcityx.algorithm.dp.*;
import com.smartcityx.algorithm.flow.*;
import com.smartcityx.algorithm.np.NPCompleteAlgorithms;
import com.smartcityx.algorithm.randomized.RandomizedAlgorithms;
import com.smartcityx.algorithm.string.*;
import com.smartcityx.algorithm.suffix.*;
import com.smartcityx.entity.AlgorithmRun;
import com.smartcityx.repository.AlgorithmRunRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Service
@Transactional
public class AlgorithmService {

    private final AlgorithmRunRepository runRepo;
    private final ObjectMapper mapper = new ObjectMapper();

    public AlgorithmService(AlgorithmRunRepository runRepo) {
        this.runRepo = runRepo;
    }

    // ── String Algorithms ────────────────────────────────────────

    public KMPAlgorithm.KMPResult runKMP(String text, String pattern) {
        KMPAlgorithm.KMPResult result = KMPAlgorithm.search(text, pattern);
        saveRun("KMP", "M1", text + "|" + pattern,
                "Matches: " + result.matchPositions.size(), result.executionTimeMs);
        return result;
    }

    public ZAlgorithm.ZResult runZ(String text, String pattern) {
        ZAlgorithm.ZResult result = ZAlgorithm.search(text, pattern);
        saveRun("Z-Function", "M1", text + "|" + pattern,
                "Matches: " + result.matchPositions.size(), result.executionTimeMs);
        return result;
    }

    public RabinKarpAlgorithm.RabinKarpResult runRabinKarp(String text, String pattern) {
        RabinKarpAlgorithm.RabinKarpResult result = RabinKarpAlgorithm.search(text, pattern);
        saveRun("Rabin-Karp", "M1", text + "|" + pattern,
                "Matches: " + result.matchPositions.size(), result.executionTimeMs);
        return result;
    }

    public AhoCorasick.AhoCorasickResult runAhoCorasick(String text, List<String> keywords) {
        AhoCorasick.AhoCorasickResult result = AhoCorasick.searchAll(text, keywords);
        saveRun("Aho-Corasick", "M1", text + "|" + keywords,
                "Occurrences: " + result.occurrences.size(), result.executionTimeMs);
        return result;
    }

    // ── Suffix Structures ────────────────────────────────────────

    public SuffixArray.SuffixArrayResult runSuffixArray(String text, String pattern) {
        SuffixArray.SuffixArrayResult result = SuffixArray.analyze(text, pattern);
        saveRun("Suffix-Array", "M2", text + "|" + pattern,
                "Matches: " + result.matchPositions.size(), result.executionTimeMs);
        return result;
    }

    public SuffixAutomaton.SAMResult runSuffixAutomaton(String text, String pattern) {
        SuffixAutomaton.SAMResult result = SuffixAutomaton.analyze(text, pattern);
        saveRun("Suffix-Automaton", "M2", text + "|" + pattern,
                "Found: " + result.patternFound, result.executionTimeMs);
        return result;
    }

    // ── DP Algorithms ────────────────────────────────────────────

    public EditDistance.EditResult runLevenshtein(String a, String b) {
        EditDistance.EditResult result = EditDistance.levenshtein(a, b);
        saveRun("Levenshtein", "M3", a + "|" + b,
                "Distance: " + result.distance, result.executionTimeMs);
        return result;
    }

    public EditDistance.EditResult runDamerauLevenshtein(String a, String b) {
        EditDistance.EditResult result = EditDistance.damerauLevenshtein(a, b);
        saveRun("Damerau-Levenshtein", "M3", a + "|" + b,
                "Distance: " + result.distance, result.executionTimeMs);
        return result;
    }

    public SubsetSumDP.SubsetSumResult runSubsetSum(int[] nums, int target) {
        SubsetSumDP.SubsetSumResult result = SubsetSumDP.solve(nums, target);
        saveRun("Subset-Sum", "M3", Arrays.toString(nums) + "|target=" + target,
                "Possible: " + result.possible, result.executionTimeMs);
        return result;
    }

    public MatrixChainDP.MatrixChainResult runMatrixChain(int[] dims) {
        MatrixChainDP.MatrixChainResult result = MatrixChainDP.solve(dims);
        saveRun("Matrix-Chain", "M3", Arrays.toString(dims),
                "Min cost: " + result.minCost, result.executionTimeMs);
        return result;
    }

    public BitmaskDP.BitmaskResult runBitmaskDP(int n, int[] costs, int[] tasks, int allTasks) {
        BitmaskDP.BitmaskResult result = BitmaskDP.solve(n, costs, tasks, allTasks);
        saveRun("Bitmask-DP", "M3", "n=" + n,
                "Min cost: " + result.minCost, result.executionTimeMs);
        return result;
    }

    // ── Network Flow ─────────────────────────────────────────────

    public FlowGraph.FlowResult runEdmondsKarp(int n, List<int[]> edgeList, int source, int sink) {
        FlowGraph g = new FlowGraph(n);
        for (int[] e : edgeList) g.addEdge(e[0], e[1], e[2]);
        FlowGraph.FlowResult result = EdmondsKarp.maxFlow(g, source, sink);
        saveRun("Edmonds-Karp", "M4", "nodes=" + n,
                "MaxFlow: " + result.maxFlow, result.executionTimeMs);
        return result;
    }

    public FlowGraph.FlowResult runDinic(int n, List<int[]> edgeList, int source, int sink) {
        FlowGraph g = new FlowGraph(n);
        for (int[] e : edgeList) g.addEdge(e[0], e[1], e[2]);
        FlowGraph.FlowResult result = DinicAlgorithm.maxFlow(g, source, sink);
        saveRun("Dinic", "M4", "nodes=" + n,
                "MaxFlow: " + result.maxFlow, result.executionTimeMs);
        return result;
    }

    public BipartiteMatching.MatchingResult runBipartiteMatching(
            List<String> left, List<String> right, List<int[]> edges) {
        BipartiteMatching.MatchingResult result = BipartiteMatching.solve(left, right, edges);
        saveRun("Bipartite-Matching", "M4", left + "<->" + right,
                "Matching: " + result.matchingSize, result.executionTimeMs);
        return result;
    }

    // ── NP-Complete ──────────────────────────────────────────────

    public NPCompleteAlgorithms.VertexCoverResult runVertexCover(int n, List<int[]> edges) {
        NPCompleteAlgorithms.Graph g = new NPCompleteAlgorithms.Graph(n, edges);
        NPCompleteAlgorithms.VertexCoverResult result = NPCompleteAlgorithms.vertexCover2Approx(g);
        saveRun("Vertex-Cover-2Approx", "M5", "n=" + n + " edges=" + edges.size(),
                "Cover size: " + result.cover.size(), result.executionTimeMs);
        return result;
    }

    public NPCompleteAlgorithms.CliqueResult runClique(int n, List<int[]> edges) {
        NPCompleteAlgorithms.Graph g = new NPCompleteAlgorithms.Graph(n, edges);
        NPCompleteAlgorithms.CliqueResult result = NPCompleteAlgorithms.maxClique(g);
        saveRun("Max-Clique", "M5", "n=" + n,
                "Clique size: " + result.size, result.executionTimeMs);
        return result;
    }

    public NPCompleteAlgorithms.IndependentSetResult runIndependentSet(int n, List<int[]> edges) {
        NPCompleteAlgorithms.Graph g = new NPCompleteAlgorithms.Graph(n, edges);
        NPCompleteAlgorithms.IndependentSetResult result = NPCompleteAlgorithms.maxIndependentSet(g);
        saveRun("Independent-Set", "M5", "n=" + n,
                "IS size: " + result.size, result.executionTimeMs);
        return result;
    }

    // ── Randomized Algorithms ────────────────────────────────────

    public RandomizedAlgorithms.QuickSortResult runRandomizedQS(int[] arr) {
        RandomizedAlgorithms.QuickSortResult result = RandomizedAlgorithms.randomizedQuickSort(arr);
        saveRun("Randomized-QuickSort", "M6", Arrays.toString(arr),
                "Comparisons: " + result.comparisons, result.executionTimeMs);
        return result;
    }

    public RandomizedAlgorithms.ReservoirResult runReservoir(int[] stream, int k) {
        RandomizedAlgorithms.ReservoirResult result = RandomizedAlgorithms.reservoirSample(stream, k);
        saveRun("Reservoir-Sampling", "M6", "stream len=" + stream.length + " k=" + k,
                Arrays.toString(result.sample), result.executionTimeMs);
        return result;
    }

    public RandomizedAlgorithms.MillerRabinResult runMillerRabin(long n, int rounds) {
        RandomizedAlgorithms.MillerRabinResult result = RandomizedAlgorithms.millerRabin(n, rounds);
        saveRun("Miller-Rabin", "M6", "n=" + n + " rounds=" + rounds,
                "Prime: " + result.isPrime, result.executionTimeMs);
        return result;
    }

    public RandomizedAlgorithms.BlellochResult runBlelloch(int[] arr) {
        RandomizedAlgorithms.BlellochResult result = RandomizedAlgorithms.blellochScan(arr);
        saveRun("Blelloch-Scan", "M6", Arrays.toString(arr),
                Arrays.toString(result.prefixSums), result.executionTimeMs);
        return result;
    }

    public RandomizedAlgorithms.ParallelReduceResult runParallelReduce(int[] arr) {
        RandomizedAlgorithms.ParallelReduceResult result = RandomizedAlgorithms.parallelReduce(arr);
        saveRun("Parallel-Reduce", "M6", Arrays.toString(arr),
                "Total: " + result.total, result.executionTimeMs);
        return result;
    }

    public List<AlgorithmRun> getRecentRuns() {
        return runRepo.findTop20ByOrderByRanAtDesc();
    }

    private void saveRun(String name, String module, String input, String result, long timeMs) {
        AlgorithmRun run = new AlgorithmRun();
        run.setAlgorithmName(name);
        run.setModule(module);
        run.setInputData(input.length() > 500 ? input.substring(0, 500) : input);
        run.setResultData(result);
        run.setExecutionTimeMs(timeMs);
        runRepo.save(run);
    }
}
