package com.smartcityx.service;

import com.smartcityx.entity.CityResource;
import com.smartcityx.repository.CityResourceRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class ResourceService {

    private final CityResourceRepository repo;

    public ResourceService(CityResourceRepository repo) {
        this.repo = repo;
    }

    public List<CityResource> findAll() {
        return repo.findAll();
    }

    public Optional<CityResource> findById(Long id) {
        return repo.findById(id);
    }

    public CityResource save(CityResource resource) {
        return repo.save(resource);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<CityResource> findByType(CityResource.ResourceType type) {
        return repo.findByResourceType(type);
    }

    public long countAvailable() {
        return repo.countAvailable();
    }

    public long countAll() {
        return repo.count();
    }
}
