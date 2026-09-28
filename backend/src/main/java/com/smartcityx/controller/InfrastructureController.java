package com.smartcityx.controller;

import com.smartcityx.service.InfrastructureService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/infrastructure")
public class InfrastructureController {

    private final InfrastructureService infrastructureService;

    public InfrastructureController(InfrastructureService infrastructureService) {
        this.infrastructureService = infrastructureService;
    }

    @GetMapping
    public String index(Model model) {
        model.addAttribute("nodes", infrastructureService.findAllNodes());
        model.addAttribute("edges", infrastructureService.findAllEdges());
        model.addAttribute("activePage", "infrastructure");
        return "infrastructure/index";
    }
}
