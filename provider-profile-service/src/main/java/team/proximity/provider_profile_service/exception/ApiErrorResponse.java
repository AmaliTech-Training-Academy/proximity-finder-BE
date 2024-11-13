package team.proximity.provider_profile_service.exception;

import java.time.LocalDateTime;

public record ApiErrorResponse(
        String path,
        String message,
        int statusCode,
        LocalDateTime timestamp
) {
}
