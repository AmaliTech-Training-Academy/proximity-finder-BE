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
    private static final String FILE_UPLOAD_ERROR = "File Upload Error";
    private static final String INVALID_FILE_TYPE = "Invalid File Type";
    private static final String PREFERENCE_NOT_FOUND = "Preference Not Found";
    private static final String RESOURCE_NOT_FOUND = "Resource Not Found";
    private static final String INTERNAL_SERVER_ERROR = "Internal Server Error";
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
        ErrorResponse errorResponse = new ErrorResponse(FILE_UPLOAD_ERROR, ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
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
        ErrorResponse errorResponse = new ErrorResponse(PREFERENCE_NOT_FOUND, ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidFileTypeException.class)
    public ResponseEntity<ApiErrorResponse> handleInvalidFileTypeException(InvalidFileTypeException ex, WebRequest request) {
        logger.error("Invalid file type: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(INVALID_FILE_TYPE, ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.UNSUPPORTED_MEDIA_TYPE);
    }
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleResourceNotFoundException(ResourceNotFoundException ex, WebRequest request) {
        logger.error("Resource not found: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(RESOURCE_NOT_FOUND, ex.getMessage());
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGlobalException(Exception ex, WebRequest request) {
        logger.error("Unexpected error: {}", ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(INTERNAL_SERVER_ERROR, "An unexpected error occurred");
        ApiErrorResponse response = ApiErrorResponse.<ErrorResponse>builder()
                .status(ApiResponseStatus.ERROR)
                .errors(Collections.singletonList(errorResponse))
                .build();
        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}