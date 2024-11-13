package team.proximity.service_provider_profile.about_your_business.about;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface AboutRepository extends JpaRepository<About, Long> {
    Optional<About> findByInceptionDate(LocalDate inceptionDate);
}
