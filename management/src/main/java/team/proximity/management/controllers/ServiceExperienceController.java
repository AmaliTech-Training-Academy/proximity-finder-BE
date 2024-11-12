package team.proximity.management.controllers;



import team.proximity.management.model.ServiceExperience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.services.ServiceExperienceService;

import java.util.List;

@RestController
@RequestMapping("/api/service-experiences")
public class ServiceExperienceController {

    private final ServiceExperienceService service;

    @Autowired
    public ServiceExperienceController(ServiceExperienceService service) {
        this.service = service;
    }

    @GetMapping
    public List<ServiceExperience> getAllServiceExperiences() {
        return service.getAllServiceExperiences();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServiceExperience> getServiceExperienceById(@PathVariable Long id) {
        return service.getServiceExperienceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ServiceExperience createServiceExperience(@RequestBody ServiceExperience serviceExperience) {
        return service.createServiceExperience(serviceExperience);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServiceExperience> updateServiceExperience(
            @PathVariable Long id,
            @RequestBody ServiceExperience serviceExperience) {
        return service.getServiceExperienceById(id)
                .map(existing -> ResponseEntity.ok(service.updateServiceExperience(id, serviceExperience)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteServiceExperience(@PathVariable Long id) {
        if (service.getServiceExperienceById(id).isPresent()) {
            service.deleteServiceExperience(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
