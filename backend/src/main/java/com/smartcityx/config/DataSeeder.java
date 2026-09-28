package com.smartcityx.config;

import com.smartcityx.entity.*;
import com.smartcityx.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Component
public class DataSeeder implements CommandLineRunner {

    private final UserRepository userRepo;
    private final RoleRepository roleRepo;
    private final ServiceRequestRepository requestRepo;
    private final SensorObservationRepository sensorRepo;
    private final CityResourceRepository resourceRepo;
    private final CityDocumentRepository documentRepo;
    private final EmergencyReportRepository emergencyRepo;
    private final InfrastructureNodeRepository nodeRepo;
    private final InfrastructureEdgeRepository edgeRepo;
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(UserRepository userRepo, RoleRepository roleRepo,
                      ServiceRequestRepository requestRepo,
                      SensorObservationRepository sensorRepo,
                      CityResourceRepository resourceRepo,
                      CityDocumentRepository documentRepo,
                      EmergencyReportRepository emergencyRepo,
                      InfrastructureNodeRepository nodeRepo,
                      InfrastructureEdgeRepository edgeRepo,
                      PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.requestRepo = requestRepo;
        this.sensorRepo = sensorRepo;
        this.resourceRepo = resourceRepo;
        this.documentRepo = documentRepo;
        this.emergencyRepo = emergencyRepo;
        this.nodeRepo = nodeRepo;
        this.edgeRepo = edgeRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepo.count() > 0) return; // already seeded

        // ── Roles ─────────────────────────────────────────────
        Role adminRole = roleRepo.save(new Role("ADMIN"));
        Role operatorRole = roleRepo.save(new Role("OPERATOR"));
        Role viewerRole = roleRepo.save(new Role("VIEWER"));

        // ── Users ─────────────────────────────────────────────
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setFullName("City Administrator");
        admin.setEmail("admin@smartcityx.gov");
        admin.setRoles(Set.of(adminRole));
        userRepo.save(admin);

        User operator = new User();
        operator.setUsername("operator");
        operator.setPassword(passwordEncoder.encode("op2024"));
        operator.setFullName("City Operator");
        operator.setEmail("operator@smartcityx.gov");
        operator.setRoles(Set.of(operatorRole));
        userRepo.save(operator);

        // ── Service Requests ──────────────────────────────────
        String[][] requestData = {
            {"Pothole on Main Street", "Large pothole causing accidents near junction 4", "ROADS", "Main Street, Zone A", "HIGH", "OPEN"},
            {"Water pipe burst near Mall", "Water gushing from underground pipe near city mall", "WATER", "City Mall Road, Zone B", "CRITICAL", "IN_PROGRESS"},
            {"Street light malfunction", "15 street lights non-functional on Highway 7", "ELECTRICITY", "Highway 7, Zone C", "MEDIUM", "OPEN"},
            {"Traffic signal timing issue", "Signal at East junction causing gridlock during peak hours", "TRAFFIC", "East Junction, Zone A", "HIGH", "IN_PROGRESS"},
            {"Garbage collection delay", "Garbage not collected in residential area for 5 days", "SANITATION", "Residential Block D", "MEDIUM", "RESOLVED"},
            {"Emergency flood situation", "Low-lying area flooded due to heavy rain", "EMERGENCY", "Zone D Lowlands", "CRITICAL", "OPEN"},
            {"Road marking faded", "Lane markings on bridge barely visible", "ROADS", "City Bridge, Zone B", "LOW", "OPEN"},
            {"Electrical transformer fault", "Transformer tripped causing power cut in Zone C", "ELECTRICITY", "Zone C Substation", "CRITICAL", "IN_PROGRESS"},
            {"Sewage overflow near school", "Sewage overflow causing health hazard near primary school", "SANITATION", "School Road, Zone A", "HIGH", "RESOLVED"},
            {"Water quality complaint", "Residents report discolored water in Zone E", "WATER", "Zone E Residential", "HIGH", "OPEN"}
        };

        for (String[] d : requestData) {
            ServiceRequest r = new ServiceRequest();
            r.setTitle(d[0]);
            r.setDescription(d[1]);
            r.setCategory(ServiceRequest.Category.valueOf(d[2]));
            r.setLocation(d[3]);
            r.setPriority(ServiceRequest.Priority.valueOf(d[4]));
            r.setStatus(ServiceRequest.Status.valueOf(d[5]));
            requestRepo.save(r);
        }

        // ── Sensor Observations ───────────────────────────────
        Object[][] sensorData = {
            {"TRF-001", SensorObservation.SensorType.TRAFFIC, "Zone A", 78.5, "vehicles/hr"},
            {"AQ-002", SensorObservation.SensorType.AIR_QUALITY, "Zone B", 145.2, "AQI"},
            {"WL-003", SensorObservation.SensorType.WATER_LEVEL, "Zone C", 4.7, "meters"},
            {"EL-004", SensorObservation.SensorType.ELECTRICITY, "Zone D", 220.3, "kW"},
            {"TMP-005", SensorObservation.SensorType.TEMPERATURE, "Zone A", 34.8, "°C"},
            {"NSE-006", SensorObservation.SensorType.NOISE, "Zone B", 72.1, "dB"},
            {"TRF-007", SensorObservation.SensorType.TRAFFIC, "Zone C", 120.0, "vehicles/hr"},
            {"WL-008", SensorObservation.SensorType.WATER_LEVEL, "Zone D", 2.3, "meters"},
            {"AQ-009", SensorObservation.SensorType.AIR_QUALITY, "Zone E", 89.0, "AQI"},
            {"EL-010", SensorObservation.SensorType.ELECTRICITY, "Zone A", 185.7, "kW"}
        };

        for (Object[] d : sensorData) {
            SensorObservation s = new SensorObservation();
            s.setSensorId((String) d[0]);
            s.setSensorType((SensorObservation.SensorType) d[1]);
            s.setZone((String) d[2]);
            s.setValue((Double) d[3]);
            s.setUnit((String) d[4]);
            s.setTimestamp(LocalDateTime.now().minusMinutes((long)(Math.random() * 60)));
            sensorRepo.save(s);
        }

        // ── City Resources ────────────────────────────────────
        Object[][] resourceData = {
            {"Ambulance Unit Alpha", CityResource.ResourceType.AMBULANCE, "Zone A", 5, 3},
            {"Ambulance Unit Beta", CityResource.ResourceType.AMBULANCE, "Zone B", 4, 2},
            {"Water Tanker Fleet 1", CityResource.ResourceType.WATER_TANKER, "Zone C", 8, 5},
            {"Water Tanker Fleet 2", CityResource.ResourceType.WATER_TANKER, "Zone D", 6, 4},
            {"Traffic Control Unit 1", CityResource.ResourceType.TRAFFIC_UNIT, "Zone A", 10, 7},
            {"Emergency Response Team Alpha", CityResource.ResourceType.EMERGENCY_TEAM, "Zone B", 3, 1},
            {"Electricity Repair Unit", CityResource.ResourceType.ELECTRICITY_UNIT, "Zone C", 6, 4},
            {"Water Supply Depot North", CityResource.ResourceType.WATER_SUPPLY, "Zone E", 1000, 650}
        };

        for (Object[] d : resourceData) {
            CityResource r = new CityResource();
            r.setName((String) d[0]);
            r.setResourceType((CityResource.ResourceType) d[1]);
            r.setZone((String) d[2]);
            r.setTotalCapacity((Integer) d[3]);
            r.setAvailableCapacity((Integer) d[4]);
            resourceRepo.save(r);
        }

        // ── City Documents ────────────────────────────────────
        String[][] docData = {
            {"Smart City Infrastructure Master Plan 2025", "INFRASTRUCTURE_PLAN", "Planning Dept",
             "This document outlines the comprehensive infrastructure development plan for SmartCityX covering roads, water, electricity and digital connectivity for the period 2025-2030."},
            {"Emergency Response Protocol Manual", "POLICY", "Emergency Dept",
             "Standard operating procedures for emergency response including fire, flood, accident and medical emergencies. All field personnel must follow these protocols."},
            {"Water Treatment Quality Report Q3-2024", "REPORT", "Water Dept",
             "Quarterly report on water quality measurements across all zones. AQI levels, contamination checks, and treatment efficiency metrics are documented herein."},
            {"Road Maintenance Tender Document 2024", "TENDER", "Roads Dept",
             "Tender specifications for road maintenance works including pothole repairs, resurfacing, lane marking and bridge inspections for fiscal year 2024."},
            {"Annual Budget Allocation Report 2024", "BUDGET", "Finance Dept",
             "Detailed budget allocation across all city departments for the year 2024. Includes capital expenditure for infrastructure and operational costs."}
        };

        for (String[] d : docData) {
            CityDocument doc = new CityDocument();
            doc.setTitle(d[0]);
            doc.setDocumentType(CityDocument.DocumentType.valueOf(d[1]));
            doc.setDepartment(d[2]);
            doc.setContent(d[3]);
            doc.setAuthor("admin");
            documentRepo.save(doc);
        }

        // ── Emergency Reports ─────────────────────────────────
        EmergencyReport er1 = new EmergencyReport();
        er1.setTitle("Flooding in Zone D");
        er1.setEmergencyType(EmergencyReport.EmergencyType.FLOOD);
        er1.setLocation("Zone D Lowlands");
        er1.setDescription("Heavy rainfall caused flooding of 3 residential streets");
        er1.setSeverity(EmergencyReport.Severity.HIGH);
        er1.setStatus("ACTIVE");
        emergencyRepo.save(er1);

        EmergencyReport er2 = new EmergencyReport();
        er2.setTitle("Power outage Zone C");
        er2.setEmergencyType(EmergencyReport.EmergencyType.POWER_OUTAGE);
        er2.setLocation("Zone C, Sector 7");
        er2.setDescription("Transformer failure caused power outage affecting 500 households");
        er2.setSeverity(EmergencyReport.Severity.CRITICAL);
        er2.setStatus("ACTIVE");
        emergencyRepo.save(er2);

        // ── Infrastructure Graph ──────────────────────────────
        Object[][] nodes = {
            {"N0", "Water Plant", "SOURCE", "Zone W", 17.3850, 78.4867},
            {"N1", "Distribution Center", "HUB", "Zone C", 17.3950, 78.4767},
            {"N2", "Zone A Supply", "DISTRIBUTION", "Zone A", 17.3750, 78.4967},
            {"N3", "Zone B Supply", "DISTRIBUTION", "Zone B", 17.3900, 78.4600},
            {"N4", "Emergency Center", "SINK", "Zone E", 17.3800, 78.5000}
        };

        for (Object[] n : nodes) {
            InfrastructureNode node = new InfrastructureNode();
            node.setNodeId((String) n[0]);
            node.setName((String) n[1]);
            node.setNodeType((String) n[2]);
            node.setZone((String) n[3]);
            node.setLatitude((Double) n[4]);
            node.setLongitude((Double) n[5]);
            nodeRepo.save(node);
        }

        int[][] edges2 = {{0, 1, 100}, {1, 2, 60}, {1, 3, 50}, {2, 4, 40}, {3, 4, 30}};
        for (int[] e : edges2) {
            InfrastructureEdge edge = new InfrastructureEdge();
            edge.setSourceNodeId("N" + e[0]);
            edge.setTargetNodeId("N" + e[1]);
            edge.setCapacity(e[2]);
            edgeRepo.save(edge);
        }

        System.out.println("✅ SmartCityX sample data seeded successfully.");
    }
}
