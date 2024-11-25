package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodCreationException;


@Component("MOBILE MONEY")
public class MobileMoneyPaymentCreator implements PaymentMethodCreator {
    @Override
    public PaymentMethod create(PaymentMethodRequest request) {
        if ( request.serviceProvider().isEmpty() || request.phoneNumber().isEmpty()) {
            throw new PaymentMethodCreationException("All fields are required: service provider, accountName, mobile number");
        }
        MobileMoneyServiceProvider provider = MobileMoneyServiceProvider.valueOf(request.serviceProvider().toUpperCase());
        MobileMoneyPayment mobileMoneyPayment = new MobileMoneyPayment();
        mobileMoneyPayment.setServiceProvider(provider);
        mobileMoneyPayment.setAccountName(request.accountName());
        mobileMoneyPayment.setAccountAlias(request.accountAlias());
        mobileMoneyPayment.setPhoneNumber(request.phoneNumber());
        return mobileMoneyPayment;
    }

    @Override
    public PaymentMethod update(PaymentMethod existing, PaymentMethodRequest request) {
        MobileMoneyPayment mobileMoneyPayment = (MobileMoneyPayment) existing;

        if (request.serviceProvider() != null) {
            MobileMoneyServiceProvider provider = MobileMoneyServiceProvider.valueOf(request.serviceProvider().toUpperCase());
            mobileMoneyPayment.setServiceProvider(provider);
        }
        if (request.accountName() != null) {
            mobileMoneyPayment.setAccountName(request.accountName());
        }
        if (request.accountAlias() != null) {
            mobileMoneyPayment.setAccountAlias(request.accountAlias());
        }
        if (request.phoneNumber() != null) {
            mobileMoneyPayment.setPhoneNumber(request.phoneNumber());
        }
        return mobileMoneyPayment;
    }
}
