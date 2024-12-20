package team.proximity.management.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.requests.ReviewRequest;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ApiSuccessResponse;
import team.proximity.management.responses.ReviewDTO;
import team.proximity.management.services.ReviewService;
import team.proximity.management.utils.AuthenticationHelper;

import java.util.List;
import java.util.Map;
import java.util.UUID;

// ReviewController.java
@RestController
@RequestMapping("/api/v1/management/reviews")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ApiSuccessResponse<ReviewDTO>> createReview(
            @RequestBody @Valid ReviewRequest request) {
        ReviewDTO review = reviewService.createReview(request, AuthenticationHelper.getCurrentUserEmail());
        ApiSuccessResponse<ReviewDTO> response = ApiSuccessResponse.<ReviewDTO>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(review)
                .build();
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    @GetMapping
    public ResponseEntity<ApiSuccessResponse<List<ReviewDTO>>> getAllReviews() {
        List<ReviewDTO> reviews = reviewService.getAllReviews();
        ApiSuccessResponse<List<ReviewDTO>> response = ApiSuccessResponse.<List<ReviewDTO>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(reviews)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }


    @GetMapping("/provider-service/{providerServiceId}")
    public ResponseEntity<ApiSuccessResponse<List<ReviewDTO>>> getProviderServiceReviews(
            @PathVariable UUID providerServiceId,
            Pageable pageable) {
        List<ReviewDTO> reviews = reviewService.getProviderServiceReviews(providerServiceId);
        ApiSuccessResponse<List<ReviewDTO>> response = ApiSuccessResponse.<List<ReviewDTO>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(reviews)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    @GetMapping("/service-provider")
    public ResponseEntity<ApiSuccessResponse<List<ReviewDTO>>> getServiceProviderReviews(
            @RequestParam String providerEmail) {
        List<ReviewDTO> reviews = reviewService.getServiceProviderReviews(providerEmail);
        ApiSuccessResponse<List<ReviewDTO>> response = ApiSuccessResponse.<List<ReviewDTO>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(reviews)
                .build();
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/service-provider/{serviceProviderId}/sentiment-analysis")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String,Object>> getServiceProviderSentimentAnalysis(
            @PathVariable UUID serviceProviderId) {
        Map<String, Object> analysis = reviewService.getServiceProviderSentimentAnalysis(serviceProviderId);
        return ResponseEntity.ok(analysis);
    }
    @GetMapping("/provider-service/{serviceProviderId}/analytics")
    public ResponseEntity<ApiSuccessResponse<Map<String, Object>>> getServiceProviderRatingAnalytics(
            @PathVariable UUID serviceProviderId) {
        Map<String, Object> analytics = reviewService.getServiceProviderRatingAnalytics(serviceProviderId);
        ApiSuccessResponse<Map<String, Object>> response = ApiSuccessResponse.<Map<String, Object>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(analytics)
                .build();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/service-provider/analytics")
    public ResponseEntity<ApiSuccessResponse<Map<String, Object>>> getProviderRatingAnalytics(
            @RequestParam String providerEmail) {
        Map<String, Object> analytics = reviewService.getProviderRatingAnalytics(providerEmail);
        ApiSuccessResponse<Map<String, Object>> response = ApiSuccessResponse.<Map<String, Object>>builder()
                .status(ApiResponseStatus.SUCCESS)
                .result(analytics)
                .build();
        return ResponseEntity.ok(response);
    }
}
