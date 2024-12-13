package team.proximity.management.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.util.stream.Collectors;

@Component
public class RequestLoggingInterceptor implements HandlerInterceptor {

    private static final Logger logger = LoggerFactory.getLogger(RequestLoggingInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        logger.info("Incoming Request: {} {}", request.getMethod(), request.getRequestURI());

        // Log headers
        request.getHeaderNames().asIterator().forEachRemaining(headerName ->
                logger.info("Header: {} = {}", headerName, request.getHeader(headerName))
        );


        return true; // Continue request processing
    }
}
