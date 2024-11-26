package team.proximity.provider_profile_service.exception.about;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team.proximity.provider_profile_service.common.ApiErrorResponse;
import team.proximity.provider_profile_service.exception.payment_method.FileTypeNotSupportedException;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestControllerAdvice
public class AboutGlobalExceptionHandler {

    private final Logger LOGGER = LoggerFactory.getLogger(AboutGlobalExceptionHandler.class);

    @ExceptionHandler(AboutAlreadyExistsException.class)
    public ResponseEntity<ApiErrorResponse> handleAboutAlreadyExistsException(AboutAlreadyExistsException exception, HttpServletRequest request) {
        LOGGER.error("About already exists: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.CONFLICT.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.CONFLICT);
    }
    @ExceptionHandler(AboutNotFoundException.class)
    public ResponseEntity<ApiErrorResponse> handleAboutNotFoundException(AboutNotFoundException exception, HttpServletRequest request) {
        LOGGER.error("About not found: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.NOT_FOUND.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }
    @ExceptionHandler(UnauthorizedAccessException.class)
    public ResponseEntity<ApiErrorResponse> handleUnauthorizedAccessException(UnauthorizedAccessException exception, HttpServletRequest request) {
        LOGGER.error("Unauthorized access: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.FORBIDDEN.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler(FileTypeNotSupportedException.class)
    public ResponseEntity<ApiErrorResponse> handleFileTypeNotSupportedException(FileTypeNotSupportedException exception, HttpServletRequest request) {
        LOGGER.error("File type not supported: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiErrorResponse> handleGenericException(Exception exception, HttpServletRequest request) {
        LOGGER.error("An unexpected error occurred: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IOException.class)
    public ResponseEntity<ApiErrorResponse> handleIOException(IOException exception, HttpServletRequest request) {
        LOGGER.error("IO Exception occurred: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.INTERNAL_SERVER_ERROR.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiErrorResponse> handleIllegalArgumentException(IllegalArgumentException exception, HttpServletRequest request) {
        LOGGER.error("Illegal argument: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(FileUploadException.class)
    public ResponseEntity<ApiErrorResponse> handleFileUploadException(FileUploadException exception, HttpServletRequest request) {
        LOGGER.error("File upload error: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserFileUploadException.class)
    public ResponseEntity<ApiErrorResponse> handleUserFileUploadException(UserFileUploadException exception, HttpServletRequest request) {
        LOGGER.error("User upload error: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(FileValidationException.class)
    public ResponseEntity<ApiErrorResponse> handleFileValidationException(FileValidationException exception, HttpServletRequest request) {
        LOGGER.error("File validation error: {}", exception.getMessage());
        ApiErrorResponse errorResponse = new ApiErrorResponse(
                request.getRequestURI(),
                exception.getMessage(),
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex, HttpServletRequest request) {
        Map<String, List<String>> fieldErrors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.groupingBy(
                        FieldError::getField,
                        Collectors.mapping(FieldError::getDefaultMessage, Collectors.toList())
                ));

        LOGGER.error("Validation failed for request to {}: {}", request.getRequestURI(), fieldErrors);
        ApiErrorResponse response = new ApiErrorResponse(
                request.getRequestURI(),
                "Validation failed: " + fieldErrors,
                HttpStatus.BAD_REQUEST.value(),
                LocalDateTime.now()
        );

        return ResponseEntity.badRequest().body(response);
    }
}
