package com.smartcityx.service;

import com.smartcityx.entity.*;
import com.smartcityx.repository.*;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional(readOnly = true)
public class DashboardService {

    private final ServiceRequestRepository requestRepo;
    private final SensorObservationRepository sensorRepo;
    private final CityResourceRepository resourceRepo;
    private final CityDocumentRepository documentRepo;
    private final EmergencyReportRepository emergencyRepo;

    public DashboardService(ServiceRequestRepository requestRepo,
                            SensorObservationRepository sensorRepo,
                            CityResourceRepository resourceRepo,
                            CityDocumentRepository documentRepo,
                            EmergencyReportRepository emergencyRepo) {
        this.requestRepo = requestRepo;
        this.sensorRepo = sensorRepo;
        this.resourceRepo = resourceRepo;
        this.documentRepo = documentRepo;
        this.emergencyRepo = emergencyRepo;
    }

    @Cacheable("dashboardStats")
    public Map<String, Object> getDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        long total = requestRepo.count();
        long open = requestRepo.countByStatus(ServiceRequest.Status.OPEN);
        long inProgress = requestRepo.countByStatus(ServiceRequest.Status.IN_PROGRESS);
        long resolved = requestRepo.countByStatus(ServiceRequest.Status.RESOLVED);
        long critical = requestRepo.countByPriority(ServiceRequest.Priority.CRITICAL);
        long sensors = sensorRepo.count();
        long resources = resourceRepo.count();
        long availableResources = resourceRepo.countAvailable();
        long documents = documentRepo.count();
        long emergencies = emergencyRepo.countByStatus("ACTIVE");

        stats.put("totalRequests", total);
        stats.put("openRequests", open);
        stats.put("inProgressRequests", inProgress);
        stats.put("resolvedRequests", resolved);
        stats.put("criticalRequests", critical);
        stats.put("totalSensors", sensors);
        stats.put("totalResources", resources);
        stats.put("availableResources", availableResources);
        stats.put("totalDocuments", documents);
        stats.put("activeEmergencies", emergencies);

        return stats;
    }

    @Cacheable("recentRequests")
    public List<ServiceRequest> getRecentRequests() {
        return requestRepo.findTop10ByOrderByCreatedAtDesc();
    }

    @Cacheable("recentSensors")
    public List<SensorObservation> getRecentSensors() {
        return sensorRepo.findTop20ByOrderByTimestampDesc();
    }

    @Cacheable("recentEmergencies")
    public List<EmergencyReport> getRecentEmergencies() {
        return emergencyRepo.findTop10ByOrderByReportedAtDesc();
    }

    public Map<String, Object> getChartData() {
        Map<String, Object> chartData = new HashMap<>();
        
        Double avgTraffic = sensorRepo.avgValueByType(SensorObservation.SensorType.TRAFFIC);
        if (avgTraffic == null) avgTraffic = 50.0;
        
        List<Double> currentTraffic = List.of(
            avgTraffic * 0.4, avgTraffic * 1.5, avgTraffic * 0.9, 
            avgTraffic * 1.2, avgTraffic * 1.8, avgTraffic * 0.6
        );
        List<Double> avgTrafficLine = List.of(
            avgTraffic * 0.3, avgTraffic * 1.2, avgTraffic * 0.8, 
            avgTraffic * 1.0, avgTraffic * 1.5, avgTraffic * 0.5
        );
        
        Map<String, Object> traffic = new HashMap<>();
        traffic.put("labels", List.of("6AM", "9AM", "12PM", "3PM", "6PM", "9PM"));
        traffic.put("current", currentTraffic);
        traffic.put("average", avgTrafficLine);
        
        Double avgWater = sensorRepo.avgValueByType(SensorObservation.SensorType.WATER_LEVEL);
        if (avgWater == null) avgWater = 300.0; 
        else avgWater = avgWater * 100; 
        
        Double avgEnergy = sensorRepo.avgValueByType(SensorObservation.SensorType.ELECTRICITY);
        if (avgEnergy == null) avgEnergy = 200.0;
        
        List<Double> waterData = List.of(
            avgWater * 1.1, avgWater * 0.9, avgWater * 1.3, avgWater * 1.2, 
            avgWater * 1.5, avgWater * 1.1, avgWater * 1.0
        );
        List<Double> energyData = List.of(
            avgEnergy * 1.0, avgEnergy * 0.95, avgEnergy * 1.4, avgEnergy * 1.3, 
            avgEnergy * 1.55, avgEnergy * 1.2, avgEnergy * 1.0
        );
        
        Map<String, Object> resource = new HashMap<>();
        resource.put("labels", List.of("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"));
        resource.put("water", waterData);
        resource.put("energy", energyData);
        
        chartData.put("traffic", traffic);
        chartData.put("resource", resource);
        
        return chartData;
    }

    public List<CityResource> getAllResources() {
        return resourceRepo.findAll();
    }

    public List<CityDocument> getAllDocuments() {
        return documentRepo.findAll();
    }

    public List<EmergencyReport> getAllEmergencies() {
        return emergencyRepo.findAll();
    }
}
