package com.smartcityx.repository;

import com.smartcityx.entity.InfrastructureNode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface InfrastructureNodeRepository extends JpaRepository<InfrastructureNode, Long> {
    Optional<InfrastructureNode> findByNodeId(String nodeId);
}
