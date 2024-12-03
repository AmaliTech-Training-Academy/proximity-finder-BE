package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
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

        if (StringUtils.hasText(request.serviceProvider())) {
            MobileMoneyServiceProvider provider = MobileMoneyServiceProvider.valueOf(request.serviceProvider().toUpperCase());
            mobileMoneyPayment.setServiceProvider(provider);
        }
        if (StringUtils.hasText(request.accountName())) {
            mobileMoneyPayment.setAccountName(request.accountName());
        }
        if (StringUtils.hasText(request.accountAlias())) {
            mobileMoneyPayment.setAccountAlias(request.accountAlias());
        }
        if (StringUtils.hasText(request.phoneNumber())) {
            mobileMoneyPayment.setPhoneNumber(request.phoneNumber());
        }
        return mobileMoneyPayment;
    }
}
