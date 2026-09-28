package com.smartcityx.controller.api;

import com.smartcityx.algorithm.dp.*;
import com.smartcityx.algorithm.flow.*;
import com.smartcityx.algorithm.np.NPCompleteAlgorithms;
import com.smartcityx.algorithm.randomized.RandomizedAlgorithms;
import com.smartcityx.algorithm.string.*;
import com.smartcityx.algorithm.suffix.*;
import com.smartcityx.service.AlgorithmService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/algorithms")
public class ApiAlgorithmController {

    private final AlgorithmService algorithmService;

    public ApiAlgorithmController(AlgorithmService algorithmService) {
        this.algorithmService = algorithmService;
    }

    // ── M1: String Search ────────────────────────────────────────

    @PostMapping("/kmp")
    public ResponseEntity<?> kmp(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        String pattern = body.getOrDefault("pattern", "");
        KMPAlgorithm.KMPResult result = algorithmService.runKMP(text, pattern);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("lpsArray", result.lpsArray);
        response.put("matchPositions", result.matchPositions);
        response.put("matchCount", result.matchPositions.size());
        response.put("comparisons", result.comparisons);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/z")
    public ResponseEntity<?> z(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        String pattern = body.getOrDefault("pattern", "");
        ZAlgorithm.ZResult result = algorithmService.runZ(text, pattern);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("zArray", result.zArray);
        response.put("matchPositions", result.matchPositions);
        response.put("matchCount", result.matchPositions.size());
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/rabin-karp")
    public ResponseEntity<?> rabinKarp(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        String pattern = body.getOrDefault("pattern", "");
        RabinKarpAlgorithm.RabinKarpResult result = algorithmService.runRabinKarp(text, pattern);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("patternHash", result.patternHash);
        response.put("matchPositions", result.matchPositions);
        response.put("spuriousHits", result.spuriousHits);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/aho-corasick")
    public ResponseEntity<?> ahoCorasick(@RequestBody Map<String, Object> body) {
        String text = (String) body.getOrDefault("text", "");
        @SuppressWarnings("unchecked")
        List<String> keywords = (List<String>) body.getOrDefault("keywords", List.of());
        AhoCorasick.AhoCorasickResult result = algorithmService.runAhoCorasick(text, keywords);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("occurrences", result.occurrences);
        response.put("trieNodeCount", result.trieNodeCount);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    // ── M2: Suffix Structures ────────────────────────────────────

    @PostMapping("/suffix-array")
    public ResponseEntity<?> suffixArray(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        String pattern = body.getOrDefault("pattern", "");
        SuffixArray.SuffixArrayResult result = algorithmService.runSuffixArray(text, pattern);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("suffixArray", result.suffixArray);
        response.put("lcpArray", result.lcpArray);
        response.put("suffixes", result.suffixes);
        response.put("matchPositions", result.matchPositions);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/suffix-automaton")
    public ResponseEntity<?> suffixAutomaton(@RequestBody Map<String, String> body) {
        String text = body.getOrDefault("text", "");
        String pattern = body.getOrDefault("pattern", "");
        SuffixAutomaton.SAMResult result = algorithmService.runSuffixAutomaton(text, pattern);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("stateCount", result.stateCount);
        response.put("patternFound", result.patternFound);
        response.put("distinctSubstrings", result.distinctSubstrings);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    // ── M3: DP ───────────────────────────────────────────────────

    @PostMapping("/levenshtein")
    public ResponseEntity<?> levenshtein(@RequestBody Map<String, String> body) {
        String a = body.getOrDefault("textA", "");
        String b = body.getOrDefault("textB", "");
        EditDistance.EditResult result = algorithmService.runLevenshtein(a, b);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("distance", result.distance);
        response.put("dpMatrix", result.dpMatrix);
        response.put("suggestion", result.suggestion);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/damerau-levenshtein")
    public ResponseEntity<?> damerauLevenshtein(@RequestBody Map<String, String> body) {
        String a = body.getOrDefault("textA", "");
        String b = body.getOrDefault("textB", "");
        EditDistance.EditResult result = algorithmService.runDamerauLevenshtein(a, b);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("distance", result.distance);
        response.put("dpMatrix", result.dpMatrix);
        response.put("suggestion", result.suggestion);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/subset-sum")
    public ResponseEntity<?> subsetSum(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> nums = (List<Integer>) body.getOrDefault("nums", List.of());
        int target = ((Number) body.getOrDefault("target", 0)).intValue();
        int[] arr = nums.stream().mapToInt(Integer::intValue).toArray();
        SubsetSumDP.SubsetSumResult result = algorithmService.runSubsetSum(arr, target);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("possible", result.possible);
        response.put("subsetFound", result.subsetFound);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/matrix-chain")
    public ResponseEntity<?> matrixChain(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> dims = (List<Integer>) body.getOrDefault("dims", List.of());
        int[] arr = dims.stream().mapToInt(Integer::intValue).toArray();
        MatrixChainDP.MatrixChainResult result = algorithmService.runMatrixChain(arr);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("minCost", result.minCost);
        response.put("optimalParenthesization", result.optimalParenthesization);
        response.put("dpTable", result.dpTable);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    // ── M4: Network Flow ─────────────────────────────────────────

    @PostMapping("/max-flow")
    public ResponseEntity<?> maxFlow(@RequestBody Map<String, Object> body) {
        int n = ((Number) body.getOrDefault("nodes", 5)).intValue();
        int source = ((Number) body.getOrDefault("source", 0)).intValue();
        int sink = ((Number) body.getOrDefault("sink", n - 1)).intValue();
        String algo = (String) body.getOrDefault("algorithm", "edmonds-karp");

        @SuppressWarnings("unchecked")
        List<List<Integer>> rawEdges = (List<List<Integer>>) body.getOrDefault("edges", List.of());
        List<int[]> edges = new ArrayList<>();
        for (List<Integer> e : rawEdges) edges.add(new int[]{e.get(0), e.get(1), e.get(2)});

        FlowGraph.FlowResult result;
        if ("dinic".equalsIgnoreCase(algo)) {
            result = algorithmService.runDinic(n, edges, source, sink);
        } else {
            result = algorithmService.runEdmondsKarp(n, edges, source, sink);
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("maxFlow", result.maxFlow);
        response.put("algorithm", result.algorithm);
        response.put("minCutSourceSide", result.minCutSource);
        response.put("executionTimeMs", result.executionTimeMs);

        List<Map<String, Object>> edgeResults = new ArrayList<>();
        for (int[] e : result.edgeFlows) {
            if (e[2] > 0) { // only forward edges
                Map<String, Object> em = new LinkedHashMap<>();
                em.put("from", e[0]); em.put("to", e[1]);
                em.put("capacity", e[2]); em.put("flow", e[3]);
                edgeResults.add(em);
            }
        }
        response.put("edges", edgeResults);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/bipartite-matching")
    public ResponseEntity<?> bipartiteMatching(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<String> left = (List<String>) body.getOrDefault("left", List.of());
        @SuppressWarnings("unchecked")
        List<String> right = (List<String>) body.getOrDefault("right", List.of());
        @SuppressWarnings("unchecked")
        List<List<Integer>> rawEdges = (List<List<Integer>>) body.getOrDefault("edges", List.of());
        List<int[]> edges = new ArrayList<>();
        for (List<Integer> e : rawEdges) edges.add(new int[]{e.get(0), e.get(1)});

        BipartiteMatching.MatchingResult result = algorithmService.runBipartiteMatching(left, right, edges);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("matchingSize", result.matchingSize);
        response.put("matchingPairs", result.matchingPairs);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    // ── M5: NP-Complete ──────────────────────────────────────────

    @PostMapping("/vertex-cover")
    public ResponseEntity<?> vertexCover(@RequestBody Map<String, Object> body) {
        int n = ((Number) body.getOrDefault("n", 6)).intValue();
        @SuppressWarnings("unchecked")
        List<List<Integer>> rawEdges = (List<List<Integer>>) body.getOrDefault("edges", List.of());
        List<int[]> edges = new ArrayList<>();
        for (List<Integer> e : rawEdges) edges.add(new int[]{e.get(0), e.get(1)});

        NPCompleteAlgorithms.VertexCoverResult result = algorithmService.runVertexCover(n, edges);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("coverNodes", result.cover);
        response.put("coverSize", result.cover.size());
        response.put("approximationRatio", result.approximationRatio);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/clique")
    public ResponseEntity<?> clique(@RequestBody Map<String, Object> body) {
        int n = ((Number) body.getOrDefault("n", 6)).intValue();
        @SuppressWarnings("unchecked")
        List<List<Integer>> rawEdges = (List<List<Integer>>) body.getOrDefault("edges", List.of());
        List<int[]> edges = new ArrayList<>();
        for (List<Integer> e : rawEdges) edges.add(new int[]{e.get(0), e.get(1)});

        NPCompleteAlgorithms.CliqueResult result = algorithmService.runClique(n, edges);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("cliqueNodes", result.clique);
        response.put("cliqueSize", result.size);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    // ── M6: Randomized ───────────────────────────────────────────

    @PostMapping("/quicksort")
    public ResponseEntity<?> quickSort(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> nums = (List<Integer>) body.getOrDefault("array", List.of());
        int[] arr = nums.stream().mapToInt(Integer::intValue).toArray();
        RandomizedAlgorithms.QuickSortResult result = algorithmService.runRandomizedQS(arr);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("sorted", result.sorted);
        response.put("comparisons", result.comparisons);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/miller-rabin")
    public ResponseEntity<?> millerRabin(@RequestBody Map<String, Object> body) {
        long n = ((Number) body.getOrDefault("n", 97)).longValue();
        int rounds = ((Number) body.getOrDefault("rounds", 10)).intValue();
        RandomizedAlgorithms.MillerRabinResult result = algorithmService.runMillerRabin(n, rounds);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("n", n);
        response.put("isPrime", result.isPrime);
        response.put("rounds", result.rounds);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/blelloch-scan")
    public ResponseEntity<?> blellochScan(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> nums = (List<Integer>) body.getOrDefault("array", List.of());
        int[] arr = nums.stream().mapToInt(Integer::intValue).toArray();
        RandomizedAlgorithms.BlellochResult result = algorithmService.runBlelloch(arr);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("prefixSums", result.prefixSums);
        response.put("totalWork", result.totalWork);
        response.put("span", result.span);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/parallel-reduce")
    public ResponseEntity<?> parallelReduce(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> nums = (List<Integer>) body.getOrDefault("array", List.of());
        int[] arr = nums.stream().mapToInt(Integer::intValue).toArray();
        RandomizedAlgorithms.ParallelReduceResult result = algorithmService.runParallelReduce(arr);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("total", result.total);
        response.put("levels", result.levels);
        response.put("work", result.work);
        response.put("span", result.span);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reservoir-sampling")
    public ResponseEntity<?> reservoirSampling(@RequestBody Map<String, Object> body) {
        @SuppressWarnings("unchecked")
        List<Integer> nums = (List<Integer>) body.getOrDefault("stream", List.of());
        int k = ((Number) body.getOrDefault("k", 5)).intValue();
        int[] arr = nums.stream().mapToInt(Integer::intValue).toArray();
        RandomizedAlgorithms.ReservoirResult result = algorithmService.runReservoir(arr, k);
        Map<String, Object> response = new LinkedHashMap<>();
        response.put("sample", result.sample);
        response.put("sampleSize", k);
        response.put("executionTimeMs", result.executionTimeMs);
        return ResponseEntity.ok(response);
    }
}
