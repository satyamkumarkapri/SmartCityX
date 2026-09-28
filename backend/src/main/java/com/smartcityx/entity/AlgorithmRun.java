package com.smartcityx.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "algorithm_runs",
        indexes = {
                @Index(name = "idx_algo_name", columnList = "algorithmName"),
                @Index(name = "idx_algo_ran_at", columnList = "ranAt")
        })
@Data
@NoArgsConstructor
public class AlgorithmRun {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String algorithmName;

    @Column(columnDefinition = "TEXT")
    private String inputData;

    @Column(columnDefinition = "TEXT")
    private String resultData;

    @Column
    private Long executionTimeMs;

    @Column(length = 50)
    private String module;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime ranAt;
}
