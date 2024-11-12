package team.proximity.management.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import team.proximity.management.requests.PreferenceRequest;
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
@RequestMapping("/api/preferences")
public class PreferenceController {
    private final ProviderServiceService providerServiceService;

    public PreferenceController(ProviderServiceService providerServiceService) {
        this.providerServiceService = providerServiceService;
    }

    @PostMapping(consumes = "multipart/form-data")
    public ResponseEntity<ApiResponse<ProviderService>> createPreference(@Validated @ModelAttribute PreferenceRequest preference) throws JsonProcessingException {
        log.info("Creating new preference with request: {}", preference);
        ProviderService createdPreference = providerServiceService.createPreference(preference);
        log.debug("Created preference: {}", createdPreference);
        ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(createdPreference)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderService>> updatePreference(@PathVariable UUID id, @RequestBody PreferenceRequest preference) {
        log.info("Updating preference with id: {} and request: {}", id, preference);
        try {
            ProviderService updatedPreference = providerServiceService.updatePreference(id, preference);
            log.debug("Updated preference: {}", updatedPreference);
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(updatedPreference)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("Preference not found with id: {}", id);
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Preference not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProviderService>> getPreferenceById(@PathVariable UUID id) {
        log.info("Fetching preference with id: {}", id);
        Optional<ProviderService> optionalPreference = providerServiceService.getPreferenceById(id);
        if (optionalPreference.isPresent()) {
            log.debug("Fetched preference: {}", optionalPreference.get());
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(optionalPreference.get())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.warn("Preference not found with id: {}", id);
            ApiResponse<ProviderService> response = ApiResponse.<ProviderService>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Preference not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProviderService>>> getAllPreferences() {
        log.info("Fetching all preferences");
        List<ProviderService> preferences = providerServiceService.getAllPreferences();
        log.debug("Fetched preferences: {}", preferences);
        ApiResponse<List<ProviderService>> response = ApiResponse.<List<ProviderService>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(preferences)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePreference(@PathVariable UUID id) {
        log.info("Deleting preference with id: {}", id);
        Optional<ProviderService> optionalPreference = providerServiceService.getPreferenceById(id);
        if (optionalPreference.isPresent()) {
            providerServiceService.deletePreference(id);
            log.debug("Deleted preference with id: {}", id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NO_CONTENT);
        } else {
            log.warn("Preference not found with id: {}", id);
            ApiResponse<Void> response = ApiResponse.<Void>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Preference not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}