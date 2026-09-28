package com.smartcityx.controller.api;

import com.smartcityx.entity.SensorObservation;
import com.smartcityx.service.SensorService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sensors")
public class ApiSensorController {

    private final SensorService service;

    public ApiSensorController(SensorService service) {
        this.service = service;
    }

    @GetMapping
    public List<SensorObservation> getAll() {
        return service.findRecent();
    }

    @GetMapping("/{id}")
    public ResponseEntity<SensorObservation> getById(@PathVariable Long id) {
        return service.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public SensorObservation create(@RequestBody SensorObservation obs) {
        return service.save(obs);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
