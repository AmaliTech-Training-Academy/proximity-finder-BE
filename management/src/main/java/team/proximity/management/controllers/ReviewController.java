package team.proximity.management.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.requests.ReviewRequest;
import team.proximity.management.responses.ReviewDTO;
import team.proximity.management.services.ReviewService;
import team.proximity.management.utils.AuthenticationHelper;

import java.util.Map;
import java.util.UUID;

// ReviewController.java
@RestController
@RequestMapping("/api/v1/reviews")
@Slf4j
public class ReviewController {
    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
//    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<ReviewDTO> createReview(
            @RequestBody @Valid ReviewRequest request) {
        ReviewDTO review = reviewService.createReview(request, AuthenticationHelper.getCurrentUserEmail());
        return ResponseEntity.ok(review);
    }

//    @PostMapping("/{reviewId}/report")
//    @PreAuthorize("hasRole('USER')")
//    public ResponseEntity<ReviewReport> reportReview(
//            @PathVariable Long reviewId,
//            @RequestParam String reason,
//            @AuthenticationPrincipal UserDetails userDetails) {
//        ReviewReport report = reviewService.reportReview(reviewId, reason, userDetails.getUsername());
//        return ResponseEntity.ok(report);
//    }

    @GetMapping("/service-provider/{serviceProviderId}")
    public ResponseEntity<Page<ReviewDTO>> getServiceProviderReviews(
            @PathVariable UUID serviceProviderId,
            Pageable pageable) {
        Page<ReviewDTO> reviews = reviewService.getServiceProviderReviews(serviceProviderId, pageable);
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/service-provider/{serviceProviderId}/sentiment-analysis")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, Object>> getServiceProviderSentimentAnalysis(
            @PathVariable UUID serviceProviderId) {
        Map<String, Object> analysis = reviewService.getServiceProviderSentimentAnalysis(serviceProviderId);
        return ResponseEntity.ok(analysis);
    }
}