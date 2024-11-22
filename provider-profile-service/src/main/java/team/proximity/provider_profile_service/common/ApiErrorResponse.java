package team.proximity.provider_profile_service.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiErrorResponse(
        String path,
        String message,
        int statusCode,
        LocalDateTime timestamp
) {

}
