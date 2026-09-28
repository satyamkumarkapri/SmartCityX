package com.smartcityx.repository;

import com.smartcityx.entity.InfrastructureEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InfrastructureEdgeRepository extends JpaRepository<InfrastructureEdge, Long> {
    List<InfrastructureEdge> findBySourceNodeId(String nodeId);
    List<InfrastructureEdge> findByTargetNodeId(String nodeId);
}
