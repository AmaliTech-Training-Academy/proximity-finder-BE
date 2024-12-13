package team.proximity.management.exceptions;

public class CustomJsonProcessingException extends RuntimeException {

    // Constructor with message only
    public CustomJsonProcessingException(String message) {
        super(message);
    }

    // Constructor with message and cause
    public CustomJsonProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    // Constructor with cause only
    public CustomJsonProcessingException(Throwable cause) {
        super(cause);
    }
}