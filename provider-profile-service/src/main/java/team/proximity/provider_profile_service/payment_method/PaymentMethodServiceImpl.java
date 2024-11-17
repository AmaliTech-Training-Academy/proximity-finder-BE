package team.proximity.provider_profile_service.payment_method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodAlreadyExistException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceDoesNotExist;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;
import team.proximity.provider_profile_service.payment_preference.PaymentPreferenceRepository;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private static final Logger logger = LoggerFactory.getLogger(PaymentMethodServiceImpl.class);

    private final PaymentMethodFactory paymentMethodFactory;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentPreferenceRepository paymentPreferenceRepository;

    public PaymentMethodServiceImpl(PaymentMethodFactory paymentMethodFactory, PaymentMethodRepository paymentMethodRepository, PaymentPreferenceRepository paymentPreferenceRepository) {
        this.paymentMethodFactory = paymentMethodFactory;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentPreferenceRepository = paymentPreferenceRepository;
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
        logger.info("Creating new payment method for user: {}", AuthHelper.getAuthenticatedUsername());

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByPreference(request.paymentPreference())
                .orElseThrow(() -> {
                    logger.error("Payment Preference not found: {}", request.paymentPreference());
                    return new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference());
                });

        paymentMethodRepository.findByCreatedByAndPaymentPreference(AuthHelper.getAuthenticatedUsername(), paymentPreference)
                .ifPresent(paymentMethod -> {
                    logger.info("Deleting existing payment method for user: {}", AuthHelper.getAuthenticatedUsername());
                    paymentMethodRepository.delete(paymentMethod);
                });

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(AuthHelper.getAuthenticatedUsername());

        paymentMethodRepository.save(paymentMethod);
        logger.info("New payment method created successfully for user: {}", AuthHelper.getAuthenticatedUsername());
    }
}
