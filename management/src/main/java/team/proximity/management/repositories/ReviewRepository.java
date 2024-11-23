package team.proximity.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team.proximity.management.model.Review;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findByProviderService(UUID serviceProviderId);
}
