package team.proximity.payment_service.payment_preference;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository

public interface PaymentPreferenceRepository extends JpaRepository<PaymentPreference, Long> {
    Optional<PaymentPreference> findByName(String name);
}
