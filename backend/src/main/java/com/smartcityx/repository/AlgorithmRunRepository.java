package com.smartcityx.repository;

import com.smartcityx.entity.AlgorithmRun;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AlgorithmRunRepository extends JpaRepository<AlgorithmRun, Long> {
    List<AlgorithmRun> findTop20ByOrderByRanAtDesc();
    List<AlgorithmRun> findByAlgorithmName(String algorithmName);
    List<AlgorithmRun> findByModule(String module);
}
