package team.proximity.provider_profile_service.exception.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import team.proximity.provider_profile_service.common.ApiErrorResponse;

import java.io.IOException;

public class AppAccessDeniedHandler implements AccessDeniedHandler {


    private static final Logger logger = LoggerFactory.getLogger(AppAccessDeniedHandler.class);

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {

        logger.error("Access denied to URI: {}", request.getRequestURI());

        ApiErrorResponse apiErrorResponse = ApiErrorResponse.builder()
                .path(request.getRequestURI())
                .message("Access denied. You do not have permission to access this resource.")
                .statusCode(HttpStatus.FORBIDDEN.value())
                .build();

        response.setStatus(HttpStatus.FORBIDDEN.value());
        response.setContentType("application/json");
        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(apiErrorResponse));
    }
}
