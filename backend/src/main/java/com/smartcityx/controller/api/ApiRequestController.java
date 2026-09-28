package com.smartcityx.controller.api;

import com.smartcityx.entity.ServiceRequest;
import com.smartcityx.service.ServiceRequestService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
public class ApiRequestController {

    private final ServiceRequestService service;

    public ApiRequestController(ServiceRequestService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceRequest> getAll(@RequestParam(required = false) String status) {
        if (status != null) return service.findByStatus(ServiceRequest.Status.valueOf(status));
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceRequest> getById(@PathVariable Long id) {
        return service.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceRequest create(@Valid @RequestBody ServiceRequest request) {
        return service.save(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceRequest> update(@PathVariable Long id,
                                                  @Valid @RequestBody ServiceRequest request) {
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        request.setId(id);
        return ResponseEntity.ok(service.save(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (service.findById(id).isEmpty()) return ResponseEntity.notFound().build();
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
