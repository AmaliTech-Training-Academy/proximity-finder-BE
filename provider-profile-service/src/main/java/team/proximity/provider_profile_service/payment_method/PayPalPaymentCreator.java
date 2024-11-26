package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodCreationException;


@Component("PAYPAL")
public class PayPalPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        if(request.email() == null || request.firstName() == null){
            throw new PaymentMethodCreationException("All fields are required: accountName, accountNumber");
        }
        PayPalPayment payPalPayment = new PayPalPayment();
        payPalPayment.setAccountName(request.accountName());
        payPalPayment.setFirstName(request.firstName());
        payPalPayment.setLastName(request.lastName());
        payPalPayment.setEmail(request.email());
        return payPalPayment;
    }
}
