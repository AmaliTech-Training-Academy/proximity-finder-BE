package team.proximity.management.responses;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ApiErrorResponse {
    private ApiResponseStatus status;
    private List<ErrorResponse> errors;
}
