package com.smartcityx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "city_resources",
        indexes = {
                @Index(name = "idx_resource_type", columnList = "resourceType"),
                @Index(name = "idx_resource_zone", columnList = "zone")
        })
@Data
@NoArgsConstructor
public class CityResource {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private ResourceType resourceType;

    @Column(nullable = false, length = 100)
    private String zone;

    @Positive
    @Column(nullable = false)
    private Integer totalCapacity;

    @Column(nullable = false)
    private Integer availableCapacity;

    @Column(length = 30)
    private String status = "AVAILABLE";

    @Column(length = 200)
    private String description;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum ResourceType {
        AMBULANCE, WATER_TANKER, TRAFFIC_UNIT, EMERGENCY_TEAM, ELECTRICITY_UNIT, WATER_SUPPLY, FIRE_TRUCK, POLICE_UNIT
    }

    public int getUtilizationPercent() {
        if (totalCapacity == 0) return 0;
        return (int) (((double)(totalCapacity - availableCapacity) / totalCapacity) * 100);
    }
}
