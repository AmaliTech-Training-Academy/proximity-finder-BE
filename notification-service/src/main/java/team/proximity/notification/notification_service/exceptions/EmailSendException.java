package team.proximity.notification.notification_service.exceptions;

public class EmailSendException extends RuntimeException {
    public EmailSendException(String message) {
        super(message);
    }
}
