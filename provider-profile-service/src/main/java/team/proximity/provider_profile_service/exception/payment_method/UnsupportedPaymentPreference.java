package team.proximity.provider_profile_service.exception.payment_method;

public class UnsupportedPaymentPreference extends RuntimeException {
    public UnsupportedPaymentPreference(String message) {
        super(message);
    }
}
