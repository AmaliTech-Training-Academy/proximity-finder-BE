package team.proximity.provider_profile_service.exception.payment_method;

public class PaymentMethodAlreadyExistException extends RuntimeException {
    public PaymentMethodAlreadyExistException(String message) {
        super(message);
    }
}
