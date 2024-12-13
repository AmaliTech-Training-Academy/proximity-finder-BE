package team.proximity.management.controllers;



import team.proximity.management.model.ServiceExperience;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.repositories.ServiceExperienceRepository;
import team.proximity.management.requests.ServiceExperienceRequest;
import team.proximity.management.services.ServiceExperienceService;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/service-experiences")
public class ServiceExperienceController {

    private final ServiceExperienceService experienceService;

    @Autowired
    public ServiceExperienceController(ServiceExperienceService experienceService, ServiceExperienceRepository serviceExperienceRepository) {
        this.experienceService = experienceService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all service experiences", description = "Returns a list of all available service experiences")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of service experiences",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceExperience.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public List<ServiceExperience> getAllServiceExperiences() {
        return experienceService.getAllServiceExperiences();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a service experience by id", description = "Returns a service experience by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved service experience by id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceExperience.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ServiceExperience> getServiceExperienceById(@PathVariable Long id) {
        return experienceService.getServiceExperienceById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create a new service experience", description = "Creates a new service experience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully created a new service experience",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceExperience.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ServiceExperience createServiceExperience( @ModelAttribute ServiceExperienceRequest serviceExperienceRequest) {
        return experienceService.createServiceExperience(serviceExperienceRequest);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update a service experience", description = "Updates a service experience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully updated service experience",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ServiceExperience.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<Optional<ServiceExperience>> updateServiceExperience(
            @PathVariable Long id,
            @RequestBody ServiceExperienceRequest serviceExperienceRequest) {
        return experienceService.getServiceExperienceById(id)
                .map(existing -> ResponseEntity.ok(experienceService.updateServiceExperience(id, serviceExperienceRequest)))
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a service experience", description = "Deletes a service experience")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted service experience"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<Void> deleteServiceExperience(@PathVariable Long id) {
        experienceService.deleteServiceExperience(id);
        return ResponseEntity.noContent().build();
    }
}
