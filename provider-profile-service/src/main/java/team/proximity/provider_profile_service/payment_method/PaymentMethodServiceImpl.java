package team.proximity.provider_profile_service.payment_method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.about.UnauthorizedAccessException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodAlreadyExistException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceDoesNotExist;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;
import team.proximity.provider_profile_service.payment_preference.PaymentPreferenceRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private final PaymentMethodMapper paymentMethodMapper;
    private final PaymentMethodFactory paymentMethodFactory;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentPreferenceRepository paymentPreferenceRepository;

    public PaymentMethodServiceImpl(PaymentMethodMapper paymentMethodMapper, PaymentMethodFactory paymentMethodFactory, PaymentMethodRepository paymentMethodRepository, PaymentPreferenceRepository paymentPreferenceRepository) {
        this.paymentMethodMapper = paymentMethodMapper;
        this.paymentMethodFactory = paymentMethodFactory;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentPreferenceRepository = paymentPreferenceRepository;
    }
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
        logger.info("Creating another payment method for user: {}", AuthHelper.getAuthenticatedUsername());

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByPreference(request.paymentPreference())
                .orElseThrow(() -> {
                    logger.error("Payment Preference not found: {}", request.paymentPreference());
                    return new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference());
                });

        if (paymentMethodRepository.findByCreatedByAndPaymentPreference(AuthHelper.getAuthenticatedUsername(), paymentPreference).isPresent()) {
            logger.warn("Payment method already exists for user: {}", AuthHelper.getAuthenticatedUsername());
            throw new PaymentMethodAlreadyExistException("Payment method already exist");
        }

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(AuthHelper.getAuthenticatedUsername());

        paymentMethodRepository.save(paymentMethod);
        logger.info("Payment method created successfully for user: {}", AuthHelper.getAuthenticatedUsername());
    }

    public void createNewPaymentMethod(PaymentMethodRequest request) {
        logger.info("Creating new payment method for user: {}", request.userEmail());

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByPreference(request.paymentPreference())
                .orElseThrow(() -> {
                    logger.error("Payment Preference not found: {}", request.paymentPreference());
                    return new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference());
                });

        paymentMethodRepository.findByCreatedByAndPaymentPreference(request.userEmail(), paymentPreference)
                .ifPresent(paymentMethod -> {
                    logger.info("Deleting existing payment method for user: {}", AuthHelper.getAuthenticatedUsername());
                    paymentMethodRepository.delete(paymentMethod);
                });

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(request.userEmail());

        paymentMethodRepository.save(paymentMethod);
        logger.info("New payment method created successfully for user: {}", request.userEmail());
    }
}
