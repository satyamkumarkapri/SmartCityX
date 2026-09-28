package com.smartcityx.service;

import com.smartcityx.entity.InfrastructureNode;
import com.smartcityx.entity.InfrastructureEdge;
import com.smartcityx.repository.InfrastructureNodeRepository;
import com.smartcityx.repository.InfrastructureEdgeRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InfrastructureService {

    private final InfrastructureNodeRepository nodeRepository;
    private final InfrastructureEdgeRepository edgeRepository;

    public InfrastructureService(InfrastructureNodeRepository nodeRepository, InfrastructureEdgeRepository edgeRepository) {
        this.nodeRepository = nodeRepository;
        this.edgeRepository = edgeRepository;
    }

    public List<InfrastructureNode> findAllNodes() {
        return nodeRepository.findAll();
    }

    public Optional<InfrastructureNode> findNodeById(Long id) {
        return nodeRepository.findById(id);
    }

    public List<InfrastructureEdge> findAllEdges() {
        return edgeRepository.findAll();
    }
}
