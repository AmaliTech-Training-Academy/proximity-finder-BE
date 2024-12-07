package team.proximity.helpandsupport.exception;


import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;

import java.io.IOException;

public class ResponseUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void writeErrorResponse(HttpServletResponse response, HttpStatus status, String message, String path) throws IOException {
        ApiErrorResponse apiErrorResponse = ApiErrorResponse.of(
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );

        response.setStatus(status.value());
        response.setContentType("application/json");
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}

