package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.payment_service.payment_method.PaymentMethod;
import team.proximity.payment_service.payment_method.PaymentMethodRequest;

@Component
public class PayPalPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        PayPalPayment payPalPayment = new PayPalPayment();
        payPalPayment.setAccountName(request.accountName());
        payPalPayment.setAccountAlias(request.accountAlias());
        payPalPayment.setAccountNumber(request.accountNumber());
        return payPalPayment;
    }
}
