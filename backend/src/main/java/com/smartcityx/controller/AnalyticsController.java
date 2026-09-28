package com.smartcityx.controller;

import com.smartcityx.service.AlgorithmService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/analytics")
public class AnalyticsController {

    private final AlgorithmService algorithmService;

    public AnalyticsController(AlgorithmService algorithmService) {
        this.algorithmService = algorithmService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("recentRuns", algorithmService.getRecentRuns());
        return "analytics/index";
    }

    @GetMapping("/string-search")
    public String stringSearch() {
        return "analytics/string-search";
    }

    @GetMapping("/fuzzy")
    public String fuzzySearch() {
        return "analytics/fuzzy";
    }

    @GetMapping("/suffix")
    public String suffix() {
        return "analytics/suffix";
    }

    @GetMapping("/dp")
    public String dp() {
        return "analytics/dp";
    }

    @GetMapping("/network-flow")
    public String networkFlow() {
        return "analytics/network-flow";
    }

    @GetMapping("/bipartite")
    public String bipartite() {
        return "analytics/bipartite";
    }

    @GetMapping("/np")
    public String np() {
        return "analytics/np";
    }

    @GetMapping("/randomized")
    public String randomized() {
        return "analytics/randomized";
    }

    @GetMapping("/parallel")
    public String parallel() {
        return "analytics/parallel";
    }
}
