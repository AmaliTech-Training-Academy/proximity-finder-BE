package team.proximity.provider_profile_service.payment_method;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;


import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {



    List<PaymentMethod> findByCreatedBy(String username);


    @Query("SELECT p FROM PaymentMethod p WHERE p.createdBy = :createdBy AND p.paymentPreference = :paymentPreference")
    Optional<PaymentMethod> findByCreatedByAndPaymentPreference(@Param("createdBy") String createdBy,
                                                                @Param("paymentPreference") PaymentPreference paymentPreference);
}
