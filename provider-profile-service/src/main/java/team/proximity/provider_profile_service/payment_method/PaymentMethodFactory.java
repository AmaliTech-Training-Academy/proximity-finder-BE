package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.bank.BankRepository;
import team.proximity.provider_profile_service.exception.payment_method.UnsupportedPaymentPreference;


import java.util.Map;

@Component
public class PaymentMethodFactory {

    private final Map<String, PaymentMethodCreator> creators;

    public PaymentMethodFactory(Map<String, PaymentMethodCreator> creators) {
      this.creators = creators;
    }

    public PaymentMethod createPaymentMethod(PaymentMethodRequest request) {
        String preference = request.paymentPreference().toUpperCase();
        PaymentMethodCreator creator = creators.get(preference);

        if (creator == null) {
            throw new UnsupportedPaymentPreference("Unsupported payment preference: " + preference);
        }
        return creator.create(request);
    }

    public PaymentMethod updatePaymentMethod(PaymentMethod existing, PaymentMethodRequest request) {
        String preference = request.paymentPreference().toUpperCase();
        PaymentMethodCreator creator = creators.get(preference);

        if (creator == null) {
            throw new UnsupportedPaymentPreference("Unsupported payment preference: " + preference);
        }

        return creator.update(existing, request);
    }
}

