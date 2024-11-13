package team.proximity.payment_service.payment_method;

import org.springframework.stereotype.Component;

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
