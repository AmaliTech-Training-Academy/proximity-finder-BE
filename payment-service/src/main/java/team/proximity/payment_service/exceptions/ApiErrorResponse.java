package team.proximity.payment_service.exceptions;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String path,
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}
