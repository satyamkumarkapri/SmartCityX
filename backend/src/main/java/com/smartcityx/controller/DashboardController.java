package com.smartcityx.controller;

import com.smartcityx.service.DashboardService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import com.smartcityx.entity.User;

@Controller
public class DashboardController {

    private final DashboardService dashboardService;
    private final com.smartcityx.repository.UserRepository userRepository;

    public DashboardController(DashboardService dashboardService, com.smartcityx.repository.UserRepository userRepository) {
        this.dashboardService = dashboardService;
        this.userRepository = userRepository;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        model.addAllAttributes(dashboardService.getDashboardStats());
        model.addAttribute("recentRequests", dashboardService.getRecentRequests());
        model.addAttribute("recentSensors", dashboardService.getRecentSensors());
        model.addAttribute("recentEmergencies", dashboardService.getRecentEmergencies());
        return "dashboard";
    }

    @GetMapping("/transportation")
    public String transportation(Model model) {
        model.addAttribute("activePage", "transportation");
        return "transportation/index";
    }

    @GetMapping("/water-energy")
    public String waterEnergy(Model model) {
        model.addAttribute("activePage", "water-energy");
        model.addAttribute("resources", dashboardService.getAllResources());
        return "water-energy";
    }

    @GetMapping("/reports")
    public String reports(Model model) {
        model.addAttribute("activePage", "reports");
        model.addAttribute("documents", dashboardService.getAllDocuments());
        return "reports";
    }

    @GetMapping("/notifications")
    public String notifications(Model model) {
        model.addAttribute("activePage", "notifications");
        model.addAttribute("emergencies", dashboardService.getAllEmergencies());
        return "notifications";
    }

    @GetMapping("/settings")
    public String settings(Model model, java.security.Principal principal) {
        model.addAttribute("activePage", "settings");
        if (principal != null) {
            userRepository.findByUsername(principal.getName()).ifPresent(user -> {
                model.addAttribute("userProfile", user);
            });
        }
        return "settings";
    }

    @PostMapping("/settings")
    public String updateSettings(
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            java.security.Principal principal) {
        if (principal != null) {
            userRepository.findByUsername(principal.getName()).ifPresent(user -> {
                user.setFullName(fullName);
                user.setPhoneNumber(phoneNumber);
                user.setAddress(address);
                userRepository.save(user);
            });
        }
        return "redirect:/settings?success";
    }
}
