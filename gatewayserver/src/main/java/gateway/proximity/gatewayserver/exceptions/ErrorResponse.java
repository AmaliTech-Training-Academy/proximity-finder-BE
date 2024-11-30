package gateway.proximity.gatewayserver.exceptions;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public record ErrorResponse(
        String timestamp,
        int status,
        String message,
        String userMessage,
        String details,
        String path,
        String traceId
) {
    private static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern("MMMM d, yyyy, HH:mm");

    public static String formatTimestamp(LocalDateTime dateTime) {
        return dateTime.format(TIMESTAMP_FORMATTER);
    }
}
