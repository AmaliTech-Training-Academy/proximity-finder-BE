package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRequest;

@Component
public class PaymentMethodValidatorFactory {

    private final BankPaymentValidator bankPaymentValidator;
    private final MobileMoneyValidator mobileMoneyValidator;
    private final PayPalPaymentValidator payPalPaymentValidator;

    public PaymentMethodValidatorFactory(
            BankPaymentValidator bankPaymentValidator,
            MobileMoneyValidator mobileMoneyValidator,
            PayPalPaymentValidator payPalPaymentValidator) {
        this.bankPaymentValidator = bankPaymentValidator;
        this.mobileMoneyValidator = mobileMoneyValidator;
        this.payPalPaymentValidator = payPalPaymentValidator;
    }

    public PaymentMethodValidator getValidator(String paymentPreference) {
        return switch (paymentPreference) {
            case "Bank Account" -> bankPaymentValidator;
            case "Mobile Money" -> mobileMoneyValidator;
            case "PayPal" -> payPalPaymentValidator;
            default -> throw new IllegalArgumentException("No validator found for payment preference: " + paymentPreference);
        };
    }
}
