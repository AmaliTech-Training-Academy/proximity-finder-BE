package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRepository;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRequest;
import team.proximity.provider_profile_service.validations.PaymentMethodValidator;

@Component
public class PayPalPaymentValidator implements PaymentMethodValidator {

    private final PaymentMethodRepository paymentMethodRepository;

    public PayPalPaymentValidator(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public void validate(PaymentMethodRequest request) {
        String username = AuthHelper.getAuthenticatedUsername();
        if (paymentMethodRepository.existsPayPalEmailForUser(request.email(), username)) {
            throw new IllegalArgumentException("PayPal email already exists for user: " + username);
        }
    }
}
