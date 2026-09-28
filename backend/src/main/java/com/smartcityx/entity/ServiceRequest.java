package com.smartcityx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "service_requests",
        indexes = {
                @Index(name = "idx_sr_status", columnList = "status"),
                @Index(name = "idx_sr_priority", columnList = "priority"),
                @Index(name = "idx_sr_category", columnList = "category")
        })
@Data
@NoArgsConstructor
public class ServiceRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 200)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private Category category;

    @Column(length = 200)
    private String location;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Priority priority = Priority.MEDIUM;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Status status = Status.OPEN;

    @Column(length = 100)
    private String assignedTo;

    @Column(columnDefinition = "TEXT")
    private String resolution;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum Category {
        ROADS, WATER, ELECTRICITY, TRAFFIC, EMERGENCY, SANITATION, OTHER
    }

    public enum Priority {
        LOW, MEDIUM, HIGH, CRITICAL
    }

    public enum Status {
        OPEN, IN_PROGRESS, RESOLVED, CLOSED
    }
}
