package team.proximity.provider_profile_service.payment_method;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodAlreadyExistException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceDoesNotExist;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;
import team.proximity.provider_profile_service.payment_preference.PaymentPreferenceRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentMethodServiceImplTest {

    @Mock
    private PaymentMethodFactory paymentMethodFactory;
    @Mock
    private PaymentMethodRepository paymentMethodRepository;
    @Mock
    private PaymentPreferenceRepository paymentPreferenceRepository;
    @Mock
    private PaymentMethod paymentMethod;
    @Mock
    private PaymentPreference paymentPreference;
    @Mock
    private PaymentMethodRequest request;

    @InjectMocks
    private PaymentMethodServiceImpl paymentMethodService;



    @Test
    void createAnotherPaymentMethodShouldSaveNewPaymentMethodWhenPaymentMethodDoesNotExist() {

        when(request.paymentPreference()).thenReturn("MOBILE_MONEY");
        when(paymentPreferenceRepository.findByPreference("MOBILE_MONEY")).thenReturn(Optional.of(paymentPreference));
        when(paymentMethodRepository.findByCreatedByAndPaymentPreference(anyString(), any())).thenReturn(Optional.empty());
        when(paymentMethodFactory.createPaymentMethod(request)).thenReturn(paymentMethod);


        paymentMethodService.createAnotherPaymentMethod(request);


        verify(paymentMethodRepository).save(paymentMethod);
        verify(paymentMethod).setPaymentPreference(paymentPreference);
        verify(paymentMethod).setCreatedBy(anyString());
    }

    @Test
    void createAnotherPaymentMethodShouldThrowExceptionWhenPaymentMethodAlreadyExists() {

        when(request.paymentPreference()).thenReturn("MOBILE_MONEY");
        when(paymentPreferenceRepository.findByPreference("MOBILE_MONEY")).thenReturn(Optional.of(paymentPreference));
        when(paymentMethodRepository.findByCreatedByAndPaymentPreference(anyString(), any())).thenReturn(Optional.of(paymentMethod));


        assertThrows(PaymentMethodAlreadyExistException.class,
            () -> paymentMethodService.createAnotherPaymentMethod(request));
    }

    @Test
    void createAnotherPaymentMethodShouldThrowExceptionWhenPaymentPreferenceNotFound() {

        when(request.paymentPreference()).thenReturn("MOBILE_MONEY");
        when(paymentPreferenceRepository.findByPreference("MOBILE_MONEY")).thenReturn(Optional.empty());


        assertThrows(PaymentPreferenceDoesNotExist.class,
            () -> paymentMethodService.createAnotherPaymentMethod(request));
    }

    @Test
    void createNewPaymentMethodShouldDeleteExistingAndSaveNewPaymentMethod() {

        when(request.paymentPreference()).thenReturn("MOBILE_MONEY");
        when(paymentPreferenceRepository.findByPreference("MOBILE_MONEY")).thenReturn(Optional.of(paymentPreference));
        when(paymentMethodRepository.findByCreatedByAndPaymentPreference(anyString(), any())).thenReturn(Optional.of(paymentMethod));
        when(paymentMethodFactory.createPaymentMethod(request)).thenReturn(paymentMethod);


        paymentMethodService.createNewPaymentMethod(request);


        verify(paymentMethodRepository).delete(paymentMethod);
        verify(paymentMethodRepository).save(paymentMethod);
        verify(paymentMethod).setPaymentPreference(paymentPreference);
        verify(paymentMethod).setCreatedBy(anyString());
    }

    @Test
    void createNewPaymentMethodShouldThrowExceptionWhenPaymentPreferenceNotFound() {

        when(request.paymentPreference()).thenReturn("MOBILE_MONEY");
        when(paymentPreferenceRepository.findByPreference("MOBILE_MONEY")).thenReturn(Optional.empty());


        assertThrows(PaymentPreferenceDoesNotExist.class,
            () -> paymentMethodService.createNewPaymentMethod(request));
    }
}
