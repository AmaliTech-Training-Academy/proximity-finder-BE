package gateway.proximity.gatewayserver.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ServerWebExchange;

import java.time.LocalDateTime;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(NoAuthorizationHeaderException.class)
    public ResponseEntity<ErrorResponse> handleNoAuthorizationHeaderException(NoAuthorizationHeaderException ex, ServerWebExchange exchange) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Missing Authorization Header",
                "The request is missing a required Authorization header. Please ensure your token is included.",
                ex.getMessage(),
                exchange
        );
    }

    @ExceptionHandler(InvalidHeaderException.class)
    public ResponseEntity<ErrorResponse> handleInvalidHeaderException(InvalidHeaderException ex, ServerWebExchange exchange) {
        return buildErrorResponse(
                HttpStatus.BAD_REQUEST,
                "Invalid Authorization Header",
                "The Authorization header format is invalid. Please use the format 'Bearer <token>'.",
                ex.getMessage(),
                exchange
        );
    }

    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException ex, ServerWebExchange exchange) {
        return buildErrorResponse(
                HttpStatus.UNAUTHORIZED,
                "Unauthorized Access",
                "Access to the requested resource is unauthorized. Ensure your token is valid and has the required permissions.",
                ex.getMessage(),
                exchange
        );
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex, ServerWebExchange exchange) {
        logger.error("Unexpected error occurred: {}", ex.getMessage(), ex);
        return buildErrorResponse(
                HttpStatus.INTERNAL_SERVER_ERROR,
                "Internal Server Error",
                "An unexpected error occurred. Please try again later or contact support if the problem persists.",
                ex.getMessage(),
                exchange
        );
    }

    private ResponseEntity<ErrorResponse> buildErrorResponse(HttpStatus status, String message, String userMessage, String details, ServerWebExchange exchange) {
        String path = exchange.getRequest().getURI().getPath();
        String traceId = exchange.getRequest().getHeaders().getFirst("X-Trace-ID");

        ErrorResponse errorResponse = new ErrorResponse(
                ErrorResponse.formatTimestamp(LocalDateTime.now()),
                status.value(),
                message,
                userMessage,
                details,
                path,
                traceId != null ? traceId : "N/A"
        );

        return new ResponseEntity<>(errorResponse, status);
    }
}
