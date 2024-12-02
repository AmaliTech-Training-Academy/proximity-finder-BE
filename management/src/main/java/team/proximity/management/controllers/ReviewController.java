package team.proximity.management.controllers;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.requests.ReviewRequest;
import team.proximity.management.responses.ReviewDTO;
import team.proximity.management.services.ReviewService;
import team.proximity.management.utils.AuthenticationHelper;

import java.util.Map;
import java.util.UUID;

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
//    @GetMapping("/{reviewId}")
//    public ResponseEntity<ReviewDTO> getReviewById(@PathVariable UUID reviewId) {
//        ReviewDTO review = reviewService.getReviewById(reviewId);
//        return ResponseEntity.ok(review);
//    }
//
//    @PutMapping("/{reviewId}")
//    public ResponseEntity<ReviewDTO> updateReview(
//            @PathVariable UUID reviewId,
//            @RequestBody @Valid ReviewRequest request) {
//        ReviewDTO updatedReview = reviewService.updateReview(reviewId, request);
//        return ResponseEntity.ok(updatedReview);
//    }
//
//    @DeleteMapping("/{reviewId}")
//    public ResponseEntity<Void> deleteReview(@PathVariable UUID reviewId) {
//        reviewService.deleteReview(reviewId);
//        return ResponseEntity.noContent().build();
//    }
//
//    @GetMapping
//    public ResponseEntity<Page<ReviewDTO>> listAllReviews(
//            @RequestParam(required = false) UUID serviceProviderId,
//            @RequestParam(required = false) Integer rating,
//            Pageable pageable) {
//        Page<ReviewDTO> reviews = reviewService.listReviews(serviceProviderId, rating, pageable);
//        return ResponseEntity.ok(reviews);
//    }
//
//    @PostMapping("/{reviewId}/helpful")
//    public ResponseEntity<Void> markAsHelpful(@PathVariable UUID reviewId) {
//        reviewService.markAsHelpful(reviewId, AuthenticationHelper.getCurrentUserEmail());
//        return ResponseEntity.ok().build();
//    }
//
//
//    @GetMapping("/my-reviews")
//    public ResponseEntity<Page<ReviewDTO>> getMyReviews(Pageable pageable) {
//        Page<ReviewDTO> myReviews = reviewService.getReviewsByUser(AuthenticationHelper.getCurrentUserEmail(), pageable);
//        return ResponseEntity.ok(myReviews);
//    }
//
//    @GetMapping("/statistics")
//    public ResponseEntity<Map<String, Object>> getReviewStatistics(
//            @RequestParam(required = false) UUID serviceProviderId) {
//        Map<String, Object> stats = reviewService.getStatistics(serviceProviderId);
//        return ResponseEntity.ok(stats);
//    }
}
