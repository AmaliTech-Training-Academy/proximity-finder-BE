package team.proximity.management.controllers;



import team.proximity.management.model.ServiceExperience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.repositories.ServiceExperienceRepository;
import team.proximity.management.requests.ServiceExperienceRequest;
import team.proximity.management.services.ServiceExperienceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/service-experiences")
public class ServiceExperienceController {

    private final ServiceExperienceService service;
    private final ServiceExperienceRepository serviceExperienceRepository;

    @Autowired
    public ServiceExperienceController(ServiceExperienceService service, ServiceExperienceRepository serviceExperienceRepository) {
        this.service = service;
        this.serviceExperienceRepository = serviceExperienceRepository;
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
    public ServiceExperience createServiceExperience(@RequestBody ServiceExperienceRequest serviceExperienceRequest) {
        return service.createServiceExperience(serviceExperienceRequest);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Optional<ServiceExperience>> updateServiceExperience(
            @PathVariable Long id,
            @RequestBody ServiceExperienceRequest serviceExperienceRequest) {
        return service.getServiceExperienceById(id)
                .map(existing -> ResponseEntity.ok(service.updateServiceExperience(id, serviceExperienceRequest)))
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
