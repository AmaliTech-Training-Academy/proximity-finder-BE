package team.proximity.management.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.proximity.management.model.ProviderService;
import team.proximity.management.model.Review;


import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProviderService(ProviderService providerService);
    List<Review> findByProviderService_UserEmail(String userEmail);
    @Query("SELECT new map(" +
            "COALESCE(AVG(r.rating), 0.0) as averageRating, " +
            "COALESCE(SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END), 0) as oneStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END), 0) as twoStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END), 0) as threeStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END), 0) as fourStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END), 0) as fiveStarCount, " +
            "COALESCE(COUNT(r), 0) as totalReviews) " +
            "FROM Review r WHERE r.providerService.id = :serviceProviderId")
    Map<String, Object> getRatingAnalyticsByServiceProvider(@Param("serviceProviderId") UUID serviceProviderId);

    @Query("SELECT new map(" +
            "COALESCE(AVG(r.rating), 0.0) as averageRating, " +
            "COALESCE(SUM(CASE WHEN r.rating = 1 THEN 1 ELSE 0 END), 0) as oneStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 2 THEN 1 ELSE 0 END), 0) as twoStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 3 THEN 1 ELSE 0 END), 0) as threeStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 4 THEN 1 ELSE 0 END), 0) as fourStarCount, " +
            "COALESCE(SUM(CASE WHEN r.rating = 5 THEN 1 ELSE 0 END), 0) as fiveStarCount, " +
            "COALESCE(COUNT(r), 0) as totalReviews) " +
            "FROM Review r WHERE r.providerService.userEmail = :providerEmail")
    Map<String, Object> getRatingAnalyticsByProviderEmail(@Param("providerEmail") String providerEmail);
}
