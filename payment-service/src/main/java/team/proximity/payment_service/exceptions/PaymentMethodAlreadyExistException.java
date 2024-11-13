package team.proximity.payment_service.exceptions;

public class PaymentMethodAlreadyExistException extends RuntimeException {
    public PaymentMethodAlreadyExistException(String message) {
        super(message);
    }
}
