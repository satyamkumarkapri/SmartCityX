package com.smartcityx.repository;

import com.smartcityx.entity.SensorObservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SensorObservationRepository extends JpaRepository<SensorObservation, Long> {
    List<SensorObservation> findBySensorType(SensorObservation.SensorType type);
    List<SensorObservation> findByZone(String zone);
    List<SensorObservation> findTop20ByOrderByTimestampDesc();

    @Query("SELECT DISTINCT s.zone FROM SensorObservation s ORDER BY s.zone")
    List<String> findAllZones();

    @Query("SELECT AVG(s.value) FROM SensorObservation s WHERE s.sensorType = :type")
    Double avgValueByType(SensorObservation.SensorType type);
}
