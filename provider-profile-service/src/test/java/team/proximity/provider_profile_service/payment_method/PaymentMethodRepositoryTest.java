package team.proximity.provider_profile_service.payment_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DataJpaTest
class PaymentMethodRepositoryTest {

    @Mock
    private PaymentMethodRepository paymentMethodRepository;

    @Test
    void findByCreatedByAndPaymentPreferenceShouldReturnPaymentMethodWhenPaymentMethodExists() {

        String createdBy = "testUser";
        PaymentPreference paymentPreference = new PaymentPreference();
        PaymentMethod expectedPaymentMethod = new PaymentMethod();
        expectedPaymentMethod.setCreatedBy(createdBy);
        expectedPaymentMethod.setPaymentPreference(paymentPreference);

        when(paymentMethodRepository.findByCreatedByAndPaymentPreference(createdBy, paymentPreference))
                .thenReturn(Optional.of(expectedPaymentMethod));

        Optional<PaymentMethod> result = paymentMethodRepository.findByCreatedByAndPaymentPreference(createdBy, paymentPreference);

        assertTrue(result.isPresent());
        assertEquals(expectedPaymentMethod, result.get());
        assertEquals(createdBy, result.get().getCreatedBy());
        assertEquals(paymentPreference, result.get().getPaymentPreference());
    }

    @Test
    void findByCreatedByAndPaymentPreferenceShouldReturnEmptyWhenPaymentMethodDoesNotExist() {

        String createdBy = "nonExistentUser";
        PaymentPreference paymentPreference = new PaymentPreference();

        when(paymentMethodRepository.findByCreatedByAndPaymentPreference(createdBy, paymentPreference))
                .thenReturn(Optional.empty());

        Optional<PaymentMethod> result = paymentMethodRepository.findByCreatedByAndPaymentPreference(createdBy, paymentPreference);

        assertTrue(result.isEmpty());
    }
}