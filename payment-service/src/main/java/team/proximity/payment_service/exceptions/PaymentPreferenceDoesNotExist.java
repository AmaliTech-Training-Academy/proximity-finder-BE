package team.proximity.payment_service.exceptions;

public class PaymentPreferenceDoesNotExist extends RuntimeException {
    public PaymentPreferenceDoesNotExist(String message) {
        super(message);
    }
}
