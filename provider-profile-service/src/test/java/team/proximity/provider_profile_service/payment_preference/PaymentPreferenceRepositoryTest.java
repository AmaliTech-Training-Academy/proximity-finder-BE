package team.proximity.provider_profile_service.payment_preference;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ExtendWith(MockitoExtension.class)
class PaymentPreferenceRepositoryTest {

    @Autowired
    private PaymentPreferenceRepository paymentPreferenceRepository;

    @Test
    void findByNameWhenPaymentPreferenceExistsReturnsPaymentPreference() {

        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setPreference("test-payment");
        paymentPreferenceRepository.save(paymentPreference);

        Optional<PaymentPreference> result = paymentPreferenceRepository.findByPreference("test-payment");

        assertTrue(result.isPresent());
        assertEquals("test-payment", result.get().getPreference());
    }

    @Test
    void findByNameWhenPaymentPreferenceDoesNotExistReturnsEmpty() {

        Optional<PaymentPreference> result = paymentPreferenceRepository.findByPreference("non-existent");

        assertTrue(result.isEmpty());
    }
}

