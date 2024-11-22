package team.proximity.provider_profile_service.payment_preference;

import org.springframework.stereotype.Component;


@Component
public class PaymentPreferenceMapper {
    public PaymentPreferenceResponse mapToPaymentPreferenceResponse(PaymentPreference paymentPreference) {
        return new PaymentPreferenceResponse(
                paymentPreference.getId(),
                paymentPreference.getPreference()
        );
    }
}
