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
    private final ProviderServiceRepository providerServiceRepository;
//    private final ReviewReportRepository reportRepository;
    private final SentimentAnalyzer sentimentAnalyzer;

    public ReviewService(ReviewRepository reviewRepository, ProviderServiceRepository providerServiceRepository, SentimentAnalyzer sentimentAnalyzer) {
        this.reviewRepository = reviewRepository;
        this.providerServiceRepository = providerServiceRepository;
//        this.reportRepository = reportRepository;
        this.sentimentAnalyzer = sentimentAnalyzer;
    }

    public ReviewDTO createReview(ReviewRequest request, String userEmail) {
//        User user = userRepository.findByEmail(userEmail)
//                .orElseThrow(() -> new UserNotFoundException("User not found"));

        ProviderService serviceProvider = providerServiceRepository.findById(request.getProviderServiceId())
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found"));

        Review review = new Review();
        review.setRating(request.getRating());
        review.setContent(request.getContent());
        review.setAnonymous(request.isAnonymous());
        review.setProviderService(serviceProvider);
        review.setAuthorEmail(request.isAnonymous() ? null : userEmail);
//        review.setPublic(request.isPublic());

        // Analyze sentiment
        String sentiment = sentimentAnalyzer.analyzeSentiment(request.getContent());
        review.setSentiment(sentiment);

        Review savedReview = reviewRepository.save(review);
        return convertToDTO(savedReview);
    }
    public List<ReviewDTO> getAllReviews() {
        List<Review> reviews = reviewRepository.findAll();
        return reviews.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public List<ReviewDTO> getProviderServiceReviews(UUID serviceProviderId) {
        ProviderService providerService = providerServiceRepository.findById(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Provider service not found"));
        List<Review> reviews = reviewRepository.findByProviderService(providerService);
        return reviews.stream()
                .map(this::convertToDTO)
                .toList();
    }

    public Map<String, Object> getServiceProviderSentimentAnalysis(UUID serviceProviderId) {
        ProviderService providerService = providerServiceRepository.findById(serviceProviderId)
                .orElseThrow(() -> new ResourceNotFoundException("Service provider not found"));
        List<Review> reviews = reviewRepository.findByProviderService(providerService);

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
    public  List<ReviewDTO> getServiceProviderReviews(String userEmail) {
        List<Review> reviews = reviewRepository.findByProviderService_UserEmail(userEmail);
        return reviews.stream()
                .map(this::convertToDTO)
                .toList();
    }


    private ReviewDTO convertToDTO(Review review) {
        ReviewDTO dto = new ReviewDTO();
        dto.setId(review.getId());
        dto.setRating(review.getRating());
        dto.setContent(review.getContent());
        dto.setAnonymous(review.isAnonymous());
        dto.setProviderServiceId(review.getProviderService().getId());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setSentiment(review.getSentiment());

        if (!review.isAnonymous()) {
            dto.setUserEmail(review.getAuthorEmail());
        }

        return dto;
    }
}