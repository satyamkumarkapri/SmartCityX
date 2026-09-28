package com.smartcityx.controller.api;

import com.smartcityx.entity.CityDocument;
import com.smartcityx.service.DocumentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/documents")
public class ApiDocumentController {

    private final DocumentService service;

    public ApiDocumentController(DocumentService service) {
        this.service = service;
    }

    @GetMapping
    public List<CityDocument> getAll(@RequestParam(required = false) String search) {
        if (search != null) return service.search(search);
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityDocument> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CityDocument create(@RequestBody CityDocument document) {
        return service.save(document);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
