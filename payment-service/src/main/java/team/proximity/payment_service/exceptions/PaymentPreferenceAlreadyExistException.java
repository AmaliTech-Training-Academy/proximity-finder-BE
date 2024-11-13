package team.proximity.payment_service.exceptions;

public class PaymentPreferenceAlreadyExistException extends RuntimeException {
    public PaymentPreferenceAlreadyExistException(String message) {
        super(message);
    }
}
