package team.proximity.management.services;


import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.proximity.management.exceptions.ResourceNotFoundException;
import team.proximity.management.model.ProviderService;
import team.proximity.management.model.Review;
import team.proximity.management.repositories.ProviderServiceRepository;
import team.proximity.management.repositories.ReviewRepository;
import team.proximity.management.requests.ReviewRequest;
import team.proximity.management.responses.ReviewDTO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@Transactional
@Slf4j
public class ReviewService {
    private final ReviewRepository reviewRepository;
//    private final UserRepository userRepository;
    private final ProviderServiceRepository serviceProviderRepository;
//    private final ReviewReportRepository reportRepository;
    private final SentimentAnalyzer sentimentAnalyzer;

    public ReviewService(ReviewRepository reviewRepository, ProviderServiceRepository serviceProviderRepository, SentimentAnalyzer sentimentAnalyzer) {
        this.reviewRepository = reviewRepository;
        this.serviceProviderRepository = serviceProviderRepository;
//        this.reportRepository = reportRepository;
        this.sentimentAnalyzer = sentimentAnalyzer;
    }

    public ReviewDTO createReview(ReviewRequest request, String userEmail) {
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProviderService serviceProvider = serviceProviderRepository.findById(request.getProviderServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found"));

        Review review = new Review();
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setAnonymous(request.isAnonymous());
        review.setProviderService(serviceProvider);
        review.setAuthorEmail(request.getAuthorEmail());

        // Analyze sentiment
        String sentiment = sentimentAnalyzer.analyzeSentiment(request.getContent());
        review.setSentiment(sentiment);

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }

//    public ReviewReport reportReview(Long reviewId, String reason, String userEmail) {
//        Review review = reviewRepository.findById(reviewId)
//                .orElseThrow(() -> new ReviewNotFoundException("Review not found"));
//
//        User reporter = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));
//
//        ReviewReport report = new ReviewReport();
//        report.setReview(review);
//        report.setReportedBy(reporter);
//        report.setReason(reason);
//
//        return reportRepository.save(report);
//    }

    public Page<ReviewDTO> getServiceProviderReviews(UUID serviceProviderId, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findByProviderService(serviceProviderId, pageable);
        return reviews.map(this::convertToDTO);
    }

    public Map<String, Object> getServiceProviderSentimentAnalysis(UUID serviceProviderId) {
        List<Review> reviews = reviewRepository.findByProviderService(serviceProviderId, Pageable.unpaged()).getContent();

        long positiveCount = reviews.stream()
                .filter(r -> "POSITIVE".equals(r.getSentiment()))
                .count();

        long negativeCount = reviews.stream()
                .filter(r -> "NEGATIVE".equals(r.getSentiment()))
                .count();

        long neutralCount = reviews.stream()
                .filter(r -> "NEUTRAL".equals(r.getSentiment()))
                .count();

        double avgRating = reviews.stream()
                .mapToInt(Review::getRating)
                .average()
                .orElse(0.0);

        Map<String, Object> analysis = new HashMap<>();
        analysis.put("totalReviews", reviews.size());
        analysis.put("positiveReviews", positiveCount);
        analysis.put("negativeReviews", negativeCount);
        analysis.put("neutralReviews", neutralCount);
        analysis.put("averageRating", avgRating);

        return analysis;
    }

    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setAnonymous(review.isAnonymous());
        dto.setServiceProviderId(review.getProviderService().getId());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setSentiment(review.getSentiment());

        if (!review.isAnonymous()) {
            dto.setUserEmail(review.getAuthorEmail());
        }

        return dto;
    }
}