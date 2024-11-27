package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class PaymentMethodValidatorFactory {

    private final Map<String, PaymentMethodValidator> validators;

    public PaymentMethodValidatorFactory(
            BankPaymentValidator bankPaymentValidator,
            MobileMoneyValidator mobileMoneyValidator,
            PayPalPaymentValidator payPalPaymentValidator) {
        this.validators = Map.of(
                "Bank Account", bankPaymentValidator,
                "Mobile Money", mobileMoneyValidator,
                "PayPal", payPalPaymentValidator
        );
    }

    public PaymentMethodValidator getValidator(String paymentPreference) {
        PaymentMethodValidator validator = validators.get(paymentPreference);
        if (validator == null) {
            throw new IllegalArgumentException("No validator found for payment preference: " + paymentPreference);
        }
        return validator;
    }
}
