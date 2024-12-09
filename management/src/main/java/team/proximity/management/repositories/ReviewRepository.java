package team.proximity.management.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import team.proximity.management.model.ProviderService;
import team.proximity.management.model.Review;


import java.util.UUID;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findByProviderService(ProviderService providerService, Pageable pageable);
}
