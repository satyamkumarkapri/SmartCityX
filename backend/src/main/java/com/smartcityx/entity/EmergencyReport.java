package com.smartcityx.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "emergency_reports",
        indexes = {
                @Index(name = "idx_emergency_type", columnList = "emergencyType"),
                @Index(name = "idx_emergency_status", columnList = "status")
        })
@Data
@NoArgsConstructor
public class EmergencyReport {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 200)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private EmergencyType emergencyType;

    @Column(nullable = false, length = 200)
    private String location;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private Severity severity = Severity.MEDIUM;

    @Column(length = 30)
    private String status = "ACTIVE";

    @Column(length = 100)
    private String respondedBy;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime reportedAt;

    @Column
    private LocalDateTime resolvedAt;

    public enum EmergencyType {
        FIRE, FLOOD, ACCIDENT, POWER_OUTAGE, GAS_LEAK, INFRASTRUCTURE_FAILURE, MEDICAL, SECURITY
    }

    public enum Severity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
}
