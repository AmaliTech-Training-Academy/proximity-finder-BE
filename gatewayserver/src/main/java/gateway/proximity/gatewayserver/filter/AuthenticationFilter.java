package gateway.proximity.gatewayserver.filter;

import gateway.proximity.gatewayserver.exceptions.InvalidHeaderException;
import gateway.proximity.gatewayserver.exceptions.NoAuthorizationHeaderException;
import gateway.proximity.gatewayserver.exceptions.UnauthorizedAccessException;
import gateway.proximity.gatewayserver.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    private static final Logger logger = LoggerFactory.getLogger(AuthenticationFilter.class);

    private final RouteValidator validator;
    private final JwtUtil jwtUtil;

    public AuthenticationFilter(RouteValidator validator, JwtUtil jwtUtil) {
        super(Config.class);
        this.validator = validator;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            logRequestDetails(request);

            if (validator.isSecured.test(request)) {
                if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new NoAuthorizationHeaderException("Missing Authorization header");
                }

                String authHeader = request.getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
                logger.info("Authorization Header Received: {}", authHeader);

                if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                    throw new InvalidHeaderException("Invalid Authorization header format");
                }

                String token = authHeader.substring(7);
                if (!jwtUtil.validateToken(token)) {
                    throw new UnauthorizedAccessException("Invalid or expired token");
                }

                logger.info("Token successfully validated for URI: {}", request.getURI());
            }

            return chain.filter(exchange);
        };
    }

    private void logRequestDetails(ServerHttpRequest request) {
        logger.info("Request Details: Method={}, URI={}, ClientIP={}",
                request.getMethod(),
                request.getURI(),
                request.getRemoteAddress());
    }

    public static class Config {
    }
}
