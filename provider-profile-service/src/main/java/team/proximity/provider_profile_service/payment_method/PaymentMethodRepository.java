package team.proximity.provider_profile_service.payment_method;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;


import java.util.List;
import java.util.Optional;

public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Long> {

    List<PaymentMethod> findByCreatedBy(String username);

    Optional<PaymentMethod> findByIdAndCreatedBy(Long id, String createdBy);

    @Query("SELECT p FROM PaymentMethod p WHERE p.createdBy = :createdBy AND p.paymentPreference = :paymentPreference")
    Optional<PaymentMethod> findByCreatedByAndPaymentPreference(@Param("createdBy") String createdBy, @Param("paymentPreference") PaymentPreference paymentPreference);

    @Query("SELECT COUNT(bp) > 0 FROM BankPayment bp WHERE bp.accountNumber = :accountNumber AND bp.createdBy = :createdBy")
    boolean existsBankAccountNumberForUser(@Param("accountNumber") String accountNumber, @Param("createdBy") String createdBy);

    @Query("SELECT COUNT(mp) > 0 FROM MobileMoneyPayment mp WHERE mp.phoneNumber = :phoneNumber AND mp.createdBy = :createdBy")
    boolean existsMobileMoneyPhoneNumberForUser(@Param("phoneNumber") String phoneNumber, @Param("createdBy") String createdBy);

    @Query("SELECT COUNT(pp) > 0 FROM PayPalPayment pp WHERE pp.email = :email AND pp.createdBy = :createdBy")
    boolean existsPayPalEmailForUser(@Param("email") String email, @Param("createdBy") String createdBy);
}

