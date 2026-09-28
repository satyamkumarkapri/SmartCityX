package com.smartcityx.service;

import com.smartcityx.entity.CityDocument;
import com.smartcityx.repository.CityDocumentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class DocumentService {

    private final CityDocumentRepository repo;

    public DocumentService(CityDocumentRepository repo) {
        this.repo = repo;
    }

    public List<CityDocument> findAll() {
        return repo.findAll();
    }

    public Optional<CityDocument> findById(Long id) {
        return repo.findById(id);
    }

    public CityDocument save(CityDocument document) {
        return repo.save(document);
    }

    public void delete(Long id) {
        repo.deleteById(id);
    }

    public List<CityDocument> search(String keyword) {
        return repo.findByTitleContainingIgnoreCaseOrContentContainingIgnoreCase(keyword, keyword);
    }

    public List<CityDocument> findRecent() {
        return repo.findTop10ByOrderByCreatedAtDesc();
    }

    public long countAll() {
        return repo.count();
    }
}
