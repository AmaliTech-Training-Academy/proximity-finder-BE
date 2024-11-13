package team.proximity.payment_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.payment_service.exceptions.UnsupportedPaymentPreference;

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

