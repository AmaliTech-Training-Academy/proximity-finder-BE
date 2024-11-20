package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodCreationException;


@Component
public class BankPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        if (request.bankName() == null || request.accountName() == null || request.bankName().isBlank()) {
            throw new PaymentMethodCreationException("All fields are required: bankName, accountName, accountNumber");
        }
        BankPayment bankPayment = new BankPayment();
        bankPayment.setBankName(request.bankName());
        bankPayment.setAccountName(request.accountName());
        bankPayment.setAccountAlias(request.accountAlias());
        bankPayment.setAccountNumber(request.accountNumber());
        return bankPayment;
    }
}
