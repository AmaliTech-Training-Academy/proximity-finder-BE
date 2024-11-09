package team.proximity.management.controllers;

import org.springframework.validation.annotation.Validated;
import team.proximity.management.requests.ServiceRequest;
import team.proximity.management.model.Services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.services.ServicesService;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/services")
public class ServicesController {
    private final ServicesService servicesService;
    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping
    public ResponseEntity<List<Services>> getAllServices() {
        List<Services> servicesList = servicesService.getAllServices();
        return new ResponseEntity<>(servicesList, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Services> getServiceById(@PathVariable UUID id) {
        return servicesService.getServiceById(id)
                .map(service -> new ResponseEntity<>(service, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Services> createService(@Validated @ModelAttribute ServiceRequest service) throws IOException {
        Services createdService = servicesService.createService(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> updateService(@PathVariable UUID id, @RequestBody ServiceRequest serviceDetails) {
        try {
            Services updatedService = servicesService.updateService(id, serviceDetails);
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable UUID id) {
        Optional<Services> product = servicesService.getServiceById(id);
        if (product.isPresent()) {
            servicesService.deleteService(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
