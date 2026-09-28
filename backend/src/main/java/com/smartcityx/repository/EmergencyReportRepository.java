package com.smartcityx.repository;

import com.smartcityx.entity.EmergencyReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmergencyReportRepository extends JpaRepository<EmergencyReport, Long> {
    List<EmergencyReport> findByStatus(String status);
    List<EmergencyReport> findBySeverity(EmergencyReport.Severity severity);
    List<EmergencyReport> findTop10ByOrderByReportedAtDesc();
    long countByStatus(String status);
}
