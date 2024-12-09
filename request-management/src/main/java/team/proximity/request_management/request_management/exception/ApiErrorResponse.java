package team.proximity.request_management.request_management.exception;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ApiErrorResponse(
        String timestamp,
        int status,
        String error,
        String message,
        String path
) {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("MMMM dd, yyyy HH:mm");

    public ApiErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now().format(FORMATTER), status, error, message, path);
    }

    public static ApiErrorResponse of(int status, String error, String message, String path) {
        return new ApiErrorResponse(status, error, message, path);
    }
}

