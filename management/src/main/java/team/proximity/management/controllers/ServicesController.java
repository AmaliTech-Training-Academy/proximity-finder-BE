package team.proximity.management.controllers;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import team.proximity.management.requests.ServiceRequest;
import team.proximity.management.model.Services;
import team.proximity.management.responses.ApiResponse;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.services.ServicesService;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/services")
public class ServicesController {
    private final ServicesService servicesService;

    public ServicesController(ServicesService servicesService) {
        this.servicesService = servicesService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Services>>> getAllServices() {
        log.info("Fetching all services");
        List<Services> servicesList = servicesService.getAllServices();
        log.debug("Fetched services: {}", servicesList);
        ApiResponse<List<Services>> response = ApiResponse.<List<Services>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(servicesList)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Services>> getServiceById(@PathVariable UUID id) {
        log.info("Fetching service with id: {}", id);
        Optional<Services> service = servicesService.getServiceById(id);
        if (service.isPresent()) {
            log.debug("Fetched service: {}", service.get());
            ApiResponse<Services> response = ApiResponse.<Services>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(service.get())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.warn("Service not found with id: {}", id);
            ApiResponse<Services> response = ApiResponse.<Services>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Service not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Services>> createService(@Validated @ModelAttribute ServiceRequest service) throws IOException {
        log.info("Creating new service with request: {}", service);
        Services createdService = servicesService.createService(service);
        log.debug("Created service: {}", createdService);
        ApiResponse<Services> response = ApiResponse.<Services>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(createdService)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Services>> updateService(@PathVariable UUID id, @RequestBody ServiceRequest serviceDetails) {
        log.info("Updating service with id: {} and request: {}", id, serviceDetails);
        try {
            Services updatedService = servicesService.updateService(id, serviceDetails);
            log.debug("Updated service: {}", updatedService);
            ApiResponse<Services> response = ApiResponse.<Services>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(updatedService)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("Service not found with id: {}", id);
            ApiResponse<Services> response = ApiResponse.<Services>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Service not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteService(@PathVariable UUID id) {
        log.info("Deleting service with id: {}", id);
        Optional<Services> service = servicesService.getServiceById(id);
        if (service.isPresent()) {
            servicesService.deleteService(id);
            log.debug("Deleted service with id: {}", id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            log.warn("Service not found with id: {}", id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Service not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

}