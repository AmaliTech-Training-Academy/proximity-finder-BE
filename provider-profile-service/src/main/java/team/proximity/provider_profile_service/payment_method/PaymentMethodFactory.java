package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.payment_service.exceptions.UnsupportedPaymentPreference;
import team.proximity.payment_service.payment_method.BankPaymentCreator;
import team.proximity.payment_service.payment_method.PayPalPaymentCreator;
import team.proximity.payment_service.payment_method.PaymentMethod;
import team.proximity.payment_service.payment_method.PaymentMethodRequest;

import java.util.Map;

@Component
public class PaymentMethodFactory {

    private final Map<String, PaymentMethodCreator> creators;

    public PaymentMethodFactory(Map<String, PaymentMethodCreator> creators) {
        this.creators = Map.of(
                "BANK ACCOUNT", new BankPaymentCreator(),
                "MOBILE MONEY", new MobileMoneyPaymentCreator(),
                "PAYPAL", new PayPalPaymentCreator()
        );
    }

    public PaymentMethod createPaymentMethod(PaymentMethodRequest request) {
        String preference = request.paymentPreference().toUpperCase();
        PaymentMethodCreator creator = creators.get(preference);

        if (creator == null) {
            throw new UnsupportedPaymentPreference("Unsupported payment preference: " + preference);
        }
        return creator.create(request);
    }
}

