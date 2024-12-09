package team.proximity.helpandsupport.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class AppAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AppAuthenticationEntryPoint.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        String uri = request.getRequestURI();
        logger.warn("Unauthorized access attempt to URI: {}", uri, authException);

        ResponseUtil.writeErrorResponse(
                response,
                HttpStatus.UNAUTHORIZED,
                "Unauthorized access. You must be authenticated to access this resource.",
                uri
        );
    }
}
