package com.smartcityx.repository;

import com.smartcityx.entity.ServiceRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ServiceRequestRepository extends JpaRepository<ServiceRequest, Long> {
    List<ServiceRequest> findByStatus(ServiceRequest.Status status);
    List<ServiceRequest> findByCategory(ServiceRequest.Category category);
    List<ServiceRequest> findByPriority(ServiceRequest.Priority priority);
    List<ServiceRequest> findTop10ByOrderByCreatedAtDesc();

    @Query("SELECT COUNT(r) FROM ServiceRequest r WHERE r.status = :status")
    long countByStatus(ServiceRequest.Status status);

    @Query("SELECT COUNT(r) FROM ServiceRequest r WHERE r.priority = :priority")
    long countByPriority(ServiceRequest.Priority priority);

    List<ServiceRequest> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
