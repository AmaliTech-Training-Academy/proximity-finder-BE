package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

    @Override
    public PaymentMethod update(PaymentMethod existing, PaymentMethodRequest request) {
        PayPalPayment payPalPayment = (PayPalPayment) existing;

        if (StringUtils.hasText(request.firstName())) {
            payPalPayment.setFirstName(request.firstName());
        }
        if (StringUtils.hasText(request.lastName())) {
            payPalPayment.setLastName(request.lastName());
        }
        if (StringUtils.hasText(request.email())) {
            payPalPayment.setEmail(request.email());
        }
        return payPalPayment;
    }
}
