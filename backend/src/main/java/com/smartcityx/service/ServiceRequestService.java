package com.smartcityx.service;

import com.smartcityx.entity.*;
import com.smartcityx.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ServiceRequestService {

    private final ServiceRequestRepository repo;

    public ServiceRequestService(ServiceRequestRepository repo) {
        this.repo = repo;
    }

    public List<ServiceRequest> findAll() {
        return repo.findAll();
    }

    public Optional<ServiceRequest> findById(Long id) {
        return repo.findById(id);
    }

    public ServiceRequest save(ServiceRequest request) {
        return repo.save(request);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<ServiceRequest> findByStatus(ServiceRequest.Status status) {
        return repo.findByStatus(status);
    }

    public List<ServiceRequest> search(String keyword) {
        return repo.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(keyword, keyword);
    }

    public List<ServiceRequest> findRecent() {
        return repo.findTop10ByOrderByCreatedAtDesc();
    }

    public long countByStatus(ServiceRequest.Status status) {
        return repo.countByStatus(status);
    }

    public long countByPriority(ServiceRequest.Priority priority) {
        return repo.countByPriority(priority);
    }
}
