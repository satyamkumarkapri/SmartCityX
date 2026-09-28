package com.smartcityx.controller.api;

import com.smartcityx.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/dashboard")
public class ApiDashboardController {

    private final DashboardService dashboardService;

    public ApiDashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping("/chart-data")
    public ResponseEntity<Map<String, Object>> getChartData() {
        return ResponseEntity.ok(dashboardService.getChartData());
    }

    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        return ResponseEntity.ok(dashboardService.getDashboardStats());
    }

    @GetMapping("/emergencies")
    public ResponseEntity<java.util.List<com.smartcityx.entity.EmergencyReport>> getEmergencies() {
        return ResponseEntity.ok(dashboardService.getAllEmergencies());
    }
}
