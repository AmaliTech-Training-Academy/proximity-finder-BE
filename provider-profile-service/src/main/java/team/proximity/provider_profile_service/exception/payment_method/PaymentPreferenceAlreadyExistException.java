package team.proximity.provider_profile_service.exception.payment_method;

public class PaymentPreferenceAlreadyExistException extends RuntimeException {
    public PaymentPreferenceAlreadyExistException(String message) {
        super(message);
    }
}
