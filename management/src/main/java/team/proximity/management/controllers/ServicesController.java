package team.proximity.management.controllers;

import team.proximity.management.dto.ServicesDTO;
import team.proximity.management.model.Services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.services.ServicesService;


import java.io.IOException;
import java.util.List;
import java.util.Optional;

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
    public ResponseEntity<Services> getServiceById(@PathVariable Long id) {
        return servicesService.getServiceById(id)
                .map(service -> new ResponseEntity<>(service, HttpStatus.OK))
                .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Services> createService(@RequestBody ServicesDTO service) throws IOException {
        Services createdService = servicesService.createService(service);
        return new ResponseEntity<>(createdService, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Services> updateService(@PathVariable Long id, @RequestBody ServicesDTO serviceDetails) {
        try {
            Services updatedService = servicesService.updateService(id, serviceDetails);
            return new ResponseEntity<>(updatedService, HttpStatus.OK);
        } catch (RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteService(@PathVariable Long id) {
        Optional<Services> product = servicesService.getServiceById(id);
        if (product.isPresent()) {
            servicesService.deleteService(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
