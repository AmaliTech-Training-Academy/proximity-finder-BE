package team.proximity.management.exceptions;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.context.request.WebRequest;
import team.proximity.management.responses.ApiResponse;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ErrorResponse errorResponse = new ErrorResponse(error.getField(), error.getDefaultMessage());
            errors.add(errorResponse);
        });
        logger.error("Validation error: {}", errors);
        ApiResponse<Map<String, String>> response = ApiResponse.<Map<String, String>>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PreferenceNotFoundException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handlePreferenceNotFoundException(PreferenceNotFoundException ex, WebRequest request) {
        logger.error("Preference not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Preference Not Found", ex.getMessage());
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleInvalidFileTypeException(InvalidFileTypeException ex, WebRequest request) {
        logger.error("Invalid file type: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Invalid File Type", ex.getMessage());
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<ErrorResponse>> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.fillInStackTrace());
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "An unexpected error occurred");
        ApiResponse<ErrorResponse> response = ApiResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}