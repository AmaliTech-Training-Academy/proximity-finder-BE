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
        LOGGER.error("Payment Preference already exists: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentPreferenceDoesNotExist.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentPreferenceDoesNotExist(PaymentPreferenceDoesNotExist exception, HttpServletRequest request) {
        LOGGER.error("Payment Preference not found: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UnsupportedPaymentPreference.class)
    public ResponseEntity<ApiErrorResponse> handleUnsupportedPaymentPreference(UnsupportedPaymentPreference exception, HttpServletRequest request) {
        LOGGER.error("Unsupported Payment Preference: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PaymentMethodAlreadyExistException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentMethodAlreadyExistException(PaymentMethodAlreadyExistException exception, HttpServletRequest request) {
        LOGGER.error("Payment Method already exists: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception, request, HttpStatus.CONFLICT);
    }

    @ExceptionHandler(PaymentMethodCreationException.class)
    public ResponseEntity<ApiErrorResponse> handlePaymentMethodCreationException(PaymentMethodCreationException exception, HttpServletRequest request) {
        LOGGER.error("Error creating payment method: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception exception, HttpServletRequest request) {
        LOGGER.error("Unexpected error occurred: {}", exception.getMessage(), exception);
        return buildErrorResponse(exception, request, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiErrorResponse> buildErrorResponse(Exception exception, HttpServletRequest request, HttpStatus status) {
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                status.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, status);
    }
}
