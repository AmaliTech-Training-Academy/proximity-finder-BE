package team.proximity.provider_profile_service.payment_method;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;


import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {
    Optional<PaymentMethod> findByAccountName(String name);

    Optional<PaymentMethod> findByCreatedBy(String currentUsername);

    @Query("SELECT p FROM PaymentMethod p WHERE p.createdBy = :createdBy AND p.paymentPreference = :paymentPreference")
    Optional<PaymentMethod> findByCreatedByAndPaymentPreference(@Param("createdBy") String createdBy,
                                                                @Param("paymentPreference") PaymentPreference paymentPreference);
}
