package team.proximity.management.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import team.proximity.management.requests.ProviderServiceRequest;
import team.proximity.management.model.ProviderService;
import team.proximity.management.responses.ApiResponse;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;
import team.proximity.management.services.ProviderServiceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@RestController
@RequestMapping("/api/providerServices")
public class ProviderServiceController {
    private final ProviderServiceService providerServiceService;

    public ProviderServiceController(ProviderServiceService providerServiceService) {
        this.providerServiceService = providerServiceService;
    }


    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderService>> updateProviderService(@PathVariable UUID id, @RequestBody ProviderServiceRequest providerService) {
        log.info("Updating providerService with id: {} and request: {}", id, providerService);
        try {
            ProviderService updatedProviderService = providerServiceService.updateProviderService(id, providerService);
            log.debug("Updated providerService: {}", updatedProviderService);
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(updatedProviderService)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("ProviderService not found with id: {}", id);
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "ProviderService not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderService>> getProviderServiceById(@PathVariable UUID id) {
        log.info("Fetching providerService with id: {}", id);
        Optional<ProviderService> optionalProviderService = providerServiceService.getProviderServiceById(id);
        if (optionalProviderService.isPresent()) {
            log.debug("Fetched providerService: {}", optionalProviderService.get());
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(optionalProviderService.get())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.warn("ProviderService not found with id: {}", id);
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "ProviderService not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderService>>> getAllProviderServices() {
        log.info("Fetching all providerServices");
        List<ProviderService> providerServices = providerServiceService.getAllProviderServices();
        log.debug("Fetched providerServices: {}", providerServices);
        ApiResponse<List<ProviderService>> response = ApiResponse.<List<ProviderService>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(providerServices)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProviderService(@PathVariable UUID id) {
        log.info("Deleting providerService with id: {}", id);
        Optional<ProviderService> optionalProviderService = providerServiceService.getProviderServiceById(id);
        if (optionalProviderService.isPresent()) {
            providerServiceService.deleteProviderService(id);
            log.debug("Deleted providerService with id: {}", id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            log.warn("ProviderService not found with id: {}", id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "ProviderService not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProviderService>> createOrUpdateProviderService(@Validated @ModelAttribute ProviderServiceRequest providerServiceRequest) throws JsonProcessingException {
        log.info("Processing providerService request: {}", providerServiceRequest);

        ProviderService providerService;
        if (providerServiceRequest.getId() != null) {
            log.info("Updating existing providerService with id: {}", providerServiceRequest.getId());
            providerService = providerServiceService.updateProviderService(providerServiceRequest.getId(), providerServiceRequest);
        } else {
            // Create new record
            log.info("Creating new providerService");
            providerService = providerServiceService.createProviderService(providerServiceRequest);
        }

        log.debug("Processed providerService: {}", providerService);
        ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(providerService)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}