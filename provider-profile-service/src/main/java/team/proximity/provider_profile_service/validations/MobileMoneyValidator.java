package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRepository;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRequest;

@Component
public class MobileMoneyValidator implements PaymentMethodValidator {

    private final PaymentMethodRepository paymentMethodRepository;

    public MobileMoneyValidator(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public void validate(PaymentMethodRequest request) {
        String username = AuthHelper.getAuthenticatedUsername();
        if (paymentMethodRepository.existsMobileMoneyPhoneNumberForUser(request.phoneNumber(), username)) {
            throw new IllegalArgumentException("Mobile Money phone number already exists for user: " + username);
        }
    }
}
