package team.proximity.provider_profile_service.payment_preference;

import org.springframework.stereotype.Component;
import team.proximity.payment_service.payment_preference.PaymentPreference;
import team.proximity.payment_service.payment_preference.PaymentPreferenceResponse;

@Component
public class PaymentPreferenceMapper {
    public PaymentPreferenceResponse mapToPaymentPreferenceResponse(PaymentPreference paymentPreference) {
        return new PaymentPreferenceResponse(
                paymentPreference.getId(),
                paymentPreference.getName()
        );
    }
}
