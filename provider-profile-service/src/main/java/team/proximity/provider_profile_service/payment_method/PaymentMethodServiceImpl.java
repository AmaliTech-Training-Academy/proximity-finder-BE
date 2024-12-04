package team.proximity.provider_profile_service.payment_method;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.about.UnauthorizedAccessException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodCreationException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceDoesNotExist;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;
import team.proximity.provider_profile_service.payment_preference.PaymentPreferenceRepository;
import team.proximity.provider_profile_service.validations.PaymentMethodValidator;
import team.proximity.provider_profile_service.validations.PaymentMethodValidatorFactory;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private  final Logger LOGGER = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private final PaymentMethodMapper paymentMethodMapper;
    private final PaymentMethodFactory paymentMethodFactory;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentPreferenceRepository paymentPreferenceRepository;
    private final PaymentMethodValidatorFactory validatorFactory;


    public List<PaymentMethodResponse> getPaymentMethodsForAuthenticatedUser() {
        String username = AuthHelper.getAuthenticatedUsername();
        if (username == null) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByCreatedBy(username);

        return paymentMethods.stream()
                .map(paymentMethodMapper::mapToResponse)
                .collect(Collectors.toList());
    }


    public void createAnotherPaymentMethod(PaymentMethodRequest request) {
        LOGGER.info("Creating another payment method for user: {}", AuthHelper.getAuthenticatedUsername());

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByPreference(request.paymentPreference())
                .orElseThrow(() -> {
                    LOGGER.error("Payment Preference not found: {}", request.paymentPreference());
                    return new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference());
                });

        PaymentMethodValidator validator = validatorFactory.getValidator(request.paymentPreference());
        validator.validate(request);

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(AuthHelper.getAuthenticatedUsername());

        paymentMethodRepository.save(paymentMethod);
        LOGGER.info("Payment method created successfully for user: {}", AuthHelper.getAuthenticatedUsername());
    }

    public void createNewPaymentMethod(PaymentMethodRequest request) {
        LOGGER.info("Creating new payment method for user: {}", AuthHelper.getAuthenticatedUsername());

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByPreference(request.paymentPreference())
                .orElseThrow(() -> {
                    LOGGER.error("Payment Preference not found: {}", request.paymentPreference());
                    return new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference());
                });

        paymentMethodRepository.findByCreatedByAndPaymentPreference(AuthHelper.getAuthenticatedUsername(), paymentPreference)
                .ifPresent(paymentMethod -> {
                    LOGGER.info("Deleting existing payment method for user: {}", AuthHelper.getAuthenticatedUsername());
                    paymentMethodRepository.delete(paymentMethod);
                });

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(AuthHelper.getAuthenticatedUsername());

        paymentMethodRepository.save(paymentMethod);
        LOGGER.info("New payment method created successfully for user: {}", AuthHelper.getAuthenticatedUsername());
    }


    @Override
    public void updatePaymentMethod(PaymentMethodRequest request, Long paymentMethodId) {
        String username = AuthHelper.getAuthenticatedUsername();

        PaymentMethod existingPaymentMethod = paymentMethodRepository.findByIdAndCreatedBy(paymentMethodId, username)
                .orElseThrow(() -> new PaymentMethodCreationException("Payment method not found for update."));

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByPreference(request.paymentPreference())
                .orElseThrow(() -> new PaymentPreferenceDoesNotExist("Payment Preference not found: " + request.paymentPreference()));

        PaymentMethod updatedPaymentMethod = paymentMethodFactory.updatePaymentMethod(existingPaymentMethod, request);
        updatedPaymentMethod.setPaymentPreference(paymentPreference);

        paymentMethodRepository.save(updatedPaymentMethod);
    }

    public void deletePaymentMethodById(Long paymentMethodId) {
        String username = AuthHelper.getAuthenticatedUsername();
        if (username == null) {
            throw new UnauthorizedAccessException("User is not authenticated");
        }

        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndCreatedBy(paymentMethodId, username)
                .orElseThrow(() -> new IllegalArgumentException("No payment method found with the given id for the authenticated user."));

        paymentMethodRepository.delete(paymentMethod);
        LOGGER.info("Payment method with id {} deleted successfully for user: {}", paymentMethodId, username);
    }



}
