package team.proximity.provider_profile_service.payment_method;

import org.springframework.data.jpa.repository.JpaRepository;
import team.proximity.payment_service.payment_method.PaymentMethod;

import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByAccountName(String name);
}
