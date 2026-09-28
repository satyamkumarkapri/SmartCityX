package com.smartcityx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "sensor_observations",
        indexes = {
                @Index(name = "idx_sensor_type", columnList = "sensorType"),
                @Index(name = "idx_sensor_zone", columnList = "zone"),
                @Index(name = "idx_sensor_timestamp", columnList = "timestamp")
        })
@Data
@NoArgsConstructor
public class SensorObservation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 50)
    private String sensorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private SensorType sensorType;

    @Column(nullable = false, length = 100)
    private String zone;

    @Column(nullable = false)
    private Double value;

    @Column(length = 20)
    private String unit;

    @Column(name = "\"timestamp\"", nullable = false)
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(length = 20)
    private String status = "NORMAL";

    @Column
    private Double minThreshold;

    @Column
    private Double maxThreshold;

    public enum SensorType {
        TRAFFIC, AIR_QUALITY, WATER_LEVEL, ELECTRICITY, TEMPERATURE, NOISE
    }
}
