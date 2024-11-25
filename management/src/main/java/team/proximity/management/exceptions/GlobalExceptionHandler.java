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
import team.proximity.management.responses.ApiErrorResponse;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;

import java.util.*;

@ControllerAdvice
public class GlobalExceptionHandler {

    private final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(BookingDayHoursValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationException(BookingDayHoursValidationException ex) {;
        ApiErrorResponse response = ApiErrorResponse.<Map<String, String>>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(ex.getErrors())
                .build();

        return ResponseEntity.badRequest().body(response);
    }
    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiErrorResponse> handleFileUploadException(FileUploadException ex) {
        logger.error("File upload error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("File Upload Error", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        List<ErrorResponse> errors = new ArrayList<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            ErrorResponse errorResponse = new ErrorResponse(error.getField(), error.getDefaultMessage());
            errors.add(errorResponse);
        });
        logger.error("Validation error: {}", errors);
        ApiErrorResponse response = ApiErrorResponse.builder()
                .status(ApiResponseStatus.ERROR)
                .errors(errors)
                .build();
        return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ProviderServiceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handlePreferenceNotFoundException(ProviderServiceNotFoundException ex, WebRequest request) {
        logger.error("Preference not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Preference Not Found", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidFileTypeException(InvalidFileTypeException ex, WebRequest request) {
        logger.error("Invalid file type: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Invalid File Type", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.error("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Resource Not Found", ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse("Internal Server Error", "An unexpected error occurred");
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}