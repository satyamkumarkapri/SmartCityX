package com.smartcityx.repository;

import com.smartcityx.entity.CityDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CityDocumentRepository extends JpaRepository<CityDocument, Long> {
    List<CityDocument> findByDocumentType(CityDocument.DocumentType type);
    List<CityDocument> findByDepartment(String department);
    List<CityDocument> findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(String title, String content);
    List<CityDocument> findTop10ByOrderByCreatedAtDesc();
}
