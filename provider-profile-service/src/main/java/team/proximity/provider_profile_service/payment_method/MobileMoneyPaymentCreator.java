package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;


@Component
public class MobileMoneyPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        MobileMoneyServiceProvider provider = MobileMoneyServiceProvider.valueOf(request.serviceProvider().toUpperCase());
        MobileMoneyPayment mobileMoneyPayment = new MobileMoneyPayment();
        mobileMoneyPayment.setServiceProvider(provider);
        mobileMoneyPayment.setAccountName(request.accountName());
        mobileMoneyPayment.setAccountAlias(request.accountAlias());
        mobileMoneyPayment.setMobileNumber(request.mobileNumber());
        return mobileMoneyPayment;
    }
}
