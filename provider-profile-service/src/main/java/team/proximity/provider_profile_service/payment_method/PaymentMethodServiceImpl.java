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
        String username = getAuthenticatedUserOrThrow();
        LOGGER.debug("Fetching payment methods for user: {}", username);

        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByCreatedBy(username);
        LOGGER.debug("Found {} payment methods for user: {}", paymentMethods.size(), username);

        return paymentMethods.stream()
                .map(paymentMethodMapper::mapToResponse)
                .collect(Collectors.toList());
    }

    public void createAnotherPaymentMethod(PaymentMethodRequest request) {
        String username = getAuthenticatedUserOrThrow();
        LOGGER.info("Creating additional payment method for user: {}", username);

        PaymentPreference preference = getPaymentPreference(request.paymentPreference());
        validatePaymentMethod(request);

        PaymentMethod paymentMethod = createAndSavePaymentMethod(request, preference, username);
        LOGGER.info("Successfully created additional payment method [id={}] for user: {}", paymentMethod.getId(), username);
    }

    public void createNewPaymentMethod(PaymentMethodRequest request) {
        String username = getAuthenticatedUserOrThrow();
        LOGGER.info("Creating new payment method for user: {}", username);

        PaymentPreference preference = getPaymentPreference(request.paymentPreference());
        deleteExistingPaymentMethod(username, preference);

        PaymentMethod paymentMethod = createAndSavePaymentMethod(request, preference, username);
        LOGGER.info("Successfully created new payment method [id={}] for user: {}", paymentMethod.getId(), username);
    }


    public void updatePaymentMethod(PaymentMethodRequest request, Long paymentMethodId) {
        String username = getAuthenticatedUserOrThrow();
        LOGGER.info("Updating payment method [id={}] for user: {}", paymentMethodId, username);

        PaymentMethod existingPaymentMethod = paymentMethodRepository.findByIdAndCreatedBy(paymentMethodId, username)
                .orElseThrow(() -> new PaymentMethodCreationException("Payment method not found for update."));

        PaymentPreference preference = getPaymentPreference(request.paymentPreference());

        PaymentMethod updatedPaymentMethod = paymentMethodFactory.updatePaymentMethod(existingPaymentMethod, request);
        updatedPaymentMethod.setPaymentPreference(preference);

        paymentMethodRepository.save(updatedPaymentMethod);
        LOGGER.info("Successfully updated payment method [id={}] for user: {}", paymentMethodId, username);
    }

    public void deletePaymentMethodById(Long paymentMethodId) {
        String username = getAuthenticatedUserOrThrow();
        LOGGER.info("Deleting payment method [id={}] for user: {}", paymentMethodId, username);

        PaymentMethod paymentMethod = paymentMethodRepository.findByIdAndCreatedBy(paymentMethodId, username)
                .orElseThrow(() -> new IllegalArgumentException("No payment method found with the given id for the authenticated user."));

        paymentMethodRepository.delete(paymentMethod);
        LOGGER.info("Successfully deleted payment method [id={}] for user: {}", paymentMethodId, username);
    }


    private String getAuthenticatedUserOrThrow() {
        String username = AuthHelper.getAuthenticatedUsername();
        if (username == null) {
            LOGGER.error("Attempted unauthorized access");
            throw new UnauthorizedAccessException("User is not authenticated");
        }
        return username;
    }

    private PaymentPreference getPaymentPreference(String preferenceName) {
        return paymentPreferenceRepository.findByPreference(preferenceName)
                .orElseThrow(() -> {
                    LOGGER.error("Payment Preference not found: {}", preferenceName);
                    return new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + preferenceName);
                });
    }

    private void validatePaymentMethod(PaymentMethodRequest request) {
        PaymentMethodValidator validator = validatorFactory.getValidator(request.paymentPreference());
        validator.validate(request);
    }

    private PaymentMethod createAndSavePaymentMethod(PaymentMethodRequest request, PaymentPreference preference, String username) {
        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(preference);
        paymentMethod.setCreatedBy(username);
        return paymentMethodRepository.save(paymentMethod);
    }

    private void deleteExistingPaymentMethod(String username, PaymentPreference preference) {
        paymentMethodRepository.findByCreatedByAndPaymentPreference(username, preference)
                .ifPresent(existingMethod -> {
                    LOGGER.debug("Deleting existing payment method [id={}] for user: {}", existingMethod.getId(), username);
                    paymentMethodRepository.delete(existingMethod);
                });
    }
}