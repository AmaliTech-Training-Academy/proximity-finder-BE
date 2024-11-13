package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.payment_service.payment_method.PaymentMethod;
import team.proximity.payment_service.payment_method.PaymentMethodCreator;
import team.proximity.payment_service.payment_method.PaymentMethodRequest;

@Component
public class MobileMoneyPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        MobileMoneyPayment mobileMoneyPayment = new MobileMoneyPayment();
        mobileMoneyPayment.setServiceProvider(request.serviceProvider());
        mobileMoneyPayment.setAccountName(request.accountName());
        mobileMoneyPayment.setAccountAlias(request.accountAlias());
        mobileMoneyPayment.setMobileNumber(request.mobileNumber());
        return mobileMoneyPayment;
    }
}