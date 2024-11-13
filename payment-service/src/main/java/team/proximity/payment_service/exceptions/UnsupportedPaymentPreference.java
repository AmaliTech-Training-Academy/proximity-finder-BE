package team.proximity.payment_service.exceptions;

public class UnsupportedPaymentPreference extends RuntimeException {
    public UnsupportedPaymentPreference(String message) {
        super(message);
    }
}
