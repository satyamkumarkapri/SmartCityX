package com.smartcityx.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "infrastructure_nodes")
@Data
@NoArgsConstructor
public class InfrastructureNode {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nodeId;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 40)
    private String nodeType;

    @Column(length = 100)
    private String zone;

    @Column
    private Double latitude;

    @Column
    private Double longitude;

    @Column(length = 30)
    private String status = "ACTIVE";
}
