package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.payment_service.payment_method.BankPayment;
import team.proximity.payment_service.payment_method.PaymentMethodRequest;

@Component
public class BankPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        BankPayment bankPayment = new BankPayment();
        bankPayment.setBankName(request.bankName());
        bankPayment.setAccountName(request.accountName());
        bankPayment.setAccountAlias(request.accountAlias());
        bankPayment.setAccountNumber(request.accountNumber());
        return bankPayment;
    }
}
