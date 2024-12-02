package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRepository;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRequest;

@Component
public class BankPaymentValidator implements PaymentMethodValidator {

    private final PaymentMethodRepository paymentMethodRepository;

    public BankPaymentValidator(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @Override
    public void validate(PaymentMethodRequest request) {
        String username = AuthHelper.getAuthenticatedUsername();
        if (paymentMethodRepository.existsBankAccountNumberForUser(request.accountNumber(), username)) {
            throw new IllegalArgumentException("Bank account number already exists for user: " + username);
        }
    }
}
