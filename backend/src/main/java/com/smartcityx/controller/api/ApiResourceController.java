package com.smartcityx.controller.api;

import com.smartcityx.entity.CityResource;
import com.smartcityx.service.ResourceService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resources")
public class ApiResourceController {

    private final ResourceService service;

    public ApiResourceController(ResourceService service) {
        this.service = service;
    }

    @GetMapping
    public List<CityResource> getAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<CityResource> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public CityResource create(@RequestBody CityResource resource) {
        return service.save(resource);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CityResource> update(@PathVariable Long id, @RequestBody CityResource resource) {
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        resource.setId(id);
        return ResponseEntity.ok(service.save(resource));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
