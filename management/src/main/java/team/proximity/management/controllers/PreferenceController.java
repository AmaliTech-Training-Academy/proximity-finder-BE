package team.proximity.management.controllers;

import lombok.extern.slf4j.Slf4j;
import team.proximity.management.requests.PreferenceRequest;
import team.proximity.management.model.Preference;
import team.proximity.management.responses.ApiResponse;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;
import team.proximity.management.services.PreferenceService;
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
    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse<Preference>> createPreference(@ModelAttribute PreferenceRequest preference) {
        log.info("Creating new preference with request: {}", preference);
        Preference createdPreference = preferenceService.createPreference(preference);
        log.debug("Created preference: {}", createdPreference);
        ApiResponse<Preference> response = ApiResponse.<Preference>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(createdPreference)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Preference>> updatePreference(@PathVariable UUID id, @RequestBody PreferenceRequest preference) {
        log.info("Updating preference with id: {} and request: {}", id, preference);
        try {
            Preference updatedPreference = preferenceService.updatePreference(id, preference);
            log.debug("Updated preference: {}", updatedPreference);
            ApiResponse<Preference> response = ApiResponse.<Preference>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(updatedPreference)
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (RuntimeException e) {
            log.warn("Preference not found with id: {}", id);
            ApiResponse<Preference> response = ApiResponse.<Preference>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Preference not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Preference>> getPreferenceById(@PathVariable UUID id) {
        log.info("Fetching preference with id: {}", id);
        Optional<Preference> optionalPreference = preferenceService.getPreferenceById(id);
        if (optionalPreference.isPresent()) {
            log.debug("Fetched preference: {}", optionalPreference.get());
            ApiResponse<Preference> response = ApiResponse.<Preference>builder()
                    .status(ApiResponseStatus.SUCCESS)
                    .result(optionalPreference.get())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            log.warn("Preference not found with id: {}", id);
            ApiResponse<Preference> response = ApiResponse.<Preference>builder()
                    .status(ApiResponseStatus.ERROR)
                    .errors(Collections.singletonList(new ErrorResponse("Not Found", "Preference not found with id " + id)))
                    .build();
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Preference>>> getAllPreferences() {
        log.info("Fetching all preferences");
        List<Preference> preferences = preferenceService.getAllPreferences();
        log.debug("Fetched preferences: {}", preferences);
        ApiResponse<List<Preference>> response = ApiResponse.<List<Preference>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(preferences)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deletePreference(@PathVariable UUID id) {
        log.info("Deleting preference with id: {}", id);
        Optional<Preference> optionalPreference = preferenceService.getPreferenceById(id);
        if (optionalPreference.isPresent()) {
            preferenceService.deletePreference(id);
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