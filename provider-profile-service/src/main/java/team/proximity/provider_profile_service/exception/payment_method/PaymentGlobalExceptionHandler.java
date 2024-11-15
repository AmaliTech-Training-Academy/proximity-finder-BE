package team.proximity.provider_profile_service.exception.payment_method;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.proximity.provider_profile_service.common.ApiErrorResponse;

import java.time.LocalDateTime;

@RestControllerAdvice
public class PaymentGlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(PaymentGlobalExceptionHandler.class);

    @ExceptionHandler(PaymentPreferenceAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentPreferenceAlreadyExistException(PaymentPreferenceAlreadyExistException exception, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );

        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PaymentPreferenceDoesNotExist.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentPreferenceDoesNotExist(PaymentPreferenceDoesNotExist exception, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(UnsupportedPaymentPreference.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedPaymentPreference(UnsupportedPaymentPreference exception, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(PaymentMethodAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentMethodAlreadyExistException(PaymentMethodAlreadyExistException exception, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(PaymentMethodCreationException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentMethodCreationException(PaymentMethodCreationException exception, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception exception, HttpServletRequest request) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );

        LOGGER.error(exception.getMessage(), exception);
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



