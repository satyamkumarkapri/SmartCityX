package com.smartcityx.service;

import com.smartcityx.entity.SensorObservation;
import com.smartcityx.repository.SensorObservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SensorService {

    private final SensorObservationRepository repo;

    public SensorService(SensorObservationRepository repo) {
        this.repo = repo;
    }

    public List<SensorObservation> findAll() {
        return repo.findAll();
    }

    public Optional<SensorObservation> findById(Long id) {
        return repo.findById(id);
    }

    public SensorObservation save(SensorObservation obs) {
        return repo.save(obs);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<SensorObservation> findRecent() {
        return repo.findTop20ByOrderByTimestampDesc();
    }

    public List<SensorObservation> findByType(SensorObservation.SensorType type) {
        return repo.findBySensorType(type);
    }

    public List<String> getAllZones() {
        return repo.findAllZones();
    }

    public long countAll() {
        return repo.count();
    }
}
