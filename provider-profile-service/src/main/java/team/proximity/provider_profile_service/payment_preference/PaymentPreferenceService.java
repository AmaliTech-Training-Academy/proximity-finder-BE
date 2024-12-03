package team.proximity.provider_profile_service.payment_preference;



import java.util.List;
import java.util.Optional;

public interface PaymentPreferenceService {

    void createOnePaymentPreference(PaymentPreferenceRequest paymentPreferenceRequest);
    Optional<PaymentPreferenceResponse> getOnePaymentPreference(String  name);
    Optional<List<PaymentPreferenceResponse>> getAllPaymentPreferences();
}
