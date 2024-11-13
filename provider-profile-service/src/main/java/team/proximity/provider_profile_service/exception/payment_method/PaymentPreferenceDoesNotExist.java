package team.proximity.provider_profile_service.exception.payment_method;

public class PaymentPreferenceDoesNotExist extends RuntimeException {
    public PaymentPreferenceDoesNotExist(String message) {
        super(message);
    }
}
