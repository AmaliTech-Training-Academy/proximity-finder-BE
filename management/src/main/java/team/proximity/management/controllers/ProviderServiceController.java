package team.proximity.management.controllers;

import com.amazonaws.Response;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import team.proximity.management.requests.ProviderServiceRequest;
import team.proximity.management.model.ProviderService;
import team.proximity.management.responses.ApiSuccessResponse;
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
@RequestMapping("/api/v1/provider-services")
public class ProviderServiceController {
    private final ProviderServiceService providerServiceService;

    public ProviderServiceController(ProviderServiceService providerServiceService) {
        this.providerServiceService = providerServiceService;
    }
    @PostMapping(consumes = "multipart/form-data")
    @Operation(summary = "Create or update a provider service", description = "Creates or updates a provider service")
    @ApiResponses(value = {
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "204", description = "Successfully deleted service experience"),
            @ApiResponse(responseCode = "201", description = "Successfully created or updated provider service",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderService.class))),
            @ApiResponse(responseCode = "400", description = "Invalid input"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ApiSuccessResponse<ProviderService>> createOrUpdateProviderService(@Validated @ModelAttribute ProviderServiceRequest providerServiceRequest) throws JsonProcessingException {
        log.info("Processing providerService request: {}", providerServiceRequest);

        ProviderService providerService = providerServiceService.createOrUpdateProviderService(providerServiceRequest);

        log.debug("Processed providerService: {}", providerService);
        ApiSuccessResponse<ProviderService> response = ApiSuccessResponse.<ProviderService>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(providerService)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Retrieve a provider service by id", description = "Returns a provider service by id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved provider service by id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderService.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ApiSuccessResponse<ProviderService>> getProviderServiceById(@PathVariable UUID id) {
        log.info("Fetching providerService with id: {}", id);

        ProviderService providerService = providerServiceService.getProviderServiceById(id);

        ApiSuccessResponse<ProviderService> response = ApiSuccessResponse.<ProviderService>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(providerService)
                .build();

        log.debug("Fetched providerService: {}", providerService);
        return ResponseEntity.ok(response);
    }
    @GetMapping("/provider/{userId}")
    @Operation(summary = "Retrieve  provider services by user id", description = "Returns a provider service by user id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved provider service by user id",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderService.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ApiSuccessResponse<List<ProviderService>>> getProviderServiceByUserId(@PathVariable String userEmail) {
        log.info("Fetching providerService with userId: {}", userEmail);

        List<ProviderService> providerServices = providerServiceService.getProviderServicesByUserEmail(userEmail);
        ApiSuccessResponse<List<ProviderService>> response = ApiSuccessResponse.<List<ProviderService>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(providerServices)
                .build();

        log.debug("Fetched providerService: {}", providerServices);
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(summary = "Retrieve all provider services", description = "Returns a list of all available provider services")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved list of provider services",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ProviderService.class))),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ApiSuccessResponse<List<ProviderService>>> getAllProviderServices() {
        log.info("Fetching all providerServices");
        List<ProviderService> providerServices = providerServiceService.getAllProviderServices();
        log.debug("Fetched providerServices: {}", providerServices);
        ApiSuccessResponse<List<ProviderService>> response = ApiSuccessResponse.<List<ProviderService>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(providerServices)
                .build();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete a provider service", description = "Deletes a provider service")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted provider service"),
            @ApiResponse(responseCode = "401", description = "You are not authorized to view the resource"),
            @ApiResponse(responseCode = "403", description = "Accessing the resource you were trying to reach is forbidden"),
            @ApiResponse(responseCode = "404", description = "The resource you were trying to reach is not found")
    })
    public ResponseEntity<ApiSuccessResponse<Void>> deleteProviderService(@PathVariable UUID id) {
        log.info("Deleting providerService with id: {}", id);
        providerServiceService.deleteProviderService(id);
        ApiSuccessResponse<Void> response = ApiSuccessResponse.<Void>builder()
                .status(ApiResponseStatus.SUCCESS)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}