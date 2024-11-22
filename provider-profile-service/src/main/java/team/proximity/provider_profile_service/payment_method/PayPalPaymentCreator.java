package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodCreationException;


@Component
public class PayPalPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        if(request.accountName() == null || request.accountNumber() == null){
            throw new PaymentMethodCreationException("All fields are required: accountName, accountNumber");
        }
        PayPalPayment payPalPayment = new PayPalPayment();
        payPalPayment.setAccountName(request.accountName());
        payPalPayment.setAccountAlias(request.accountAlias());
        payPalPayment.setAccountNumber(request.accountNumber());
        return payPalPayment;
    }
}
