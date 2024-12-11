package team.proximity.notification.notification_service.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EmailSendException.class)
    public ResponseEntity<ErrorResponse> handleEmailSendException(EmailSendException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.SERVICE_UNAVAILABLE.value(),
                "Email Service Error",
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.SERVICE_UNAVAILABLE);
    }

    @ExceptionHandler(NotificationProcessingException.class)
    public ResponseEntity<ErrorResponse> handleNotificationProcessingException(NotificationProcessingException ex) {
        ErrorResponse errorResponse = new ErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                "Notification Processing Error",
                ex.getMessage(),
                System.currentTimeMillis()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }


}
