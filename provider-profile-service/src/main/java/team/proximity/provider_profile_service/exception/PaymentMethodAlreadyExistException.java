package team.proximity.provider_profile_service.exception;

public class PaymentMethodAlreadyExistException extends RuntimeException {
    public PaymentMethodAlreadyExistException(String message) {
        super(message);
    }
}
