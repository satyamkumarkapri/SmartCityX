package com.smartcityx.repository;

import com.smartcityx.entity.CityResource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityResourceRepository extends JpaRepository<CityResource, Long> {
    List<CityResource> findByResourceType(CityResource.ResourceType type);
    List<CityResource> findByZone(String zone);
    List<CityResource> findByStatus(String status);

    @Query("SELECT COUNT(r) FROM CityResource r WHERE r.availableCapacity > 0")
    long countAvailable();
}
