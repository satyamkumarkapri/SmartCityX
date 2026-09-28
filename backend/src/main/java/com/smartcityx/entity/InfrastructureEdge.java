package com.smartcityx.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "infrastructure_edges",
        indexes = {
                @Index(name = "idx_edge_source", columnList = "sourceNodeId"),
                @Index(name = "idx_edge_target", columnList = "targetNodeId")
        })
@Data
@NoArgsConstructor
public class InfrastructureEdge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String sourceNodeId;

    @Column(nullable = false, length = 100)
    private String targetNodeId;

    @Column(nullable = false)
    private Integer capacity;

    @Column
    private Integer currentFlow = 0;

    @Column(length = 50)
    private String edgeType;
}
