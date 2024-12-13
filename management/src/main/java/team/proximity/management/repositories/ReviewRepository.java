package team.proximity.management.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.proximity.management.model.ProviderService;
import team.proximity.management.model.Review;


import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProviderService(ProviderService providerService);
    List<Review> findByProviderService_UserEmail(String userEmail);
    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.providerService = :providerService")
    Double findAverageRatingByProviderService(@Param("providerService") ProviderService providerService);

    @Query("SELECT r.rating, COUNT(r) FROM Review r WHERE r.providerService = :providerService GROUP BY r.rating")
    List<Object[]> findRatingDistributionByProviderService(@Param("providerService") ProviderService providerService);
}
