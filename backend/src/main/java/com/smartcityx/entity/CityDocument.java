package com.smartcityx.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "city_documents",
        indexes = {
                @Index(name = "idx_doc_type", columnList = "documentType"),
                @Index(name = "idx_doc_dept", columnList = "department")
        })
@Data
@NoArgsConstructor
public class CityDocument {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(nullable = false, length = 300)
    private String title;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private DocumentType documentType;

    @Column(nullable = false, length = 100)
    private String department;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Column(length = 50)
    private String status = "ACTIVE";

    @Column(length = 50)
    private String author;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    public enum DocumentType {
        POLICY, REPORT, GUIDELINE, INCIDENT_REPORT, MAINTENANCE_LOG, INFRASTRUCTURE_PLAN, BUDGET, TENDER
    }
}
