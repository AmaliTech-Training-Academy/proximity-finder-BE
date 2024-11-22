package gateway.proximity.gatewayserver.filter;


import gateway.proximity.gatewayserver.exceptions.InvalidHeaderException;
import gateway.proximity.gatewayserver.exceptions.NoAuthorizationHeaderException;
import gateway.proximity.gatewayserver.exceptions.UnauthorizedAccessException;
import gateway.proximity.gatewayserver.util.JwtUtil;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

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

            if (validator.isSecured.test(exchange.getRequest())) {
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    throw new NoAuthorizationHeaderException("Missing Authorization header");
                }

                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);

                if (authHeader != null && authHeader.startsWith("Bearer ")) {
                    authHeader = authHeader.substring(7);
                } else {
                    throw new InvalidHeaderException("Invalid Authorization header format");
                }

                try {

                    boolean isValid = jwtUtil.validateToken(authHeader);
                    if (!isValid) {
                        throw new UnauthorizedAccessException("Unauthorized access to the application");
                    }

                } catch (Exception e) {
                    throw new UnauthorizedAccessException("Unauthorized access to the application");
                }
            }
            return chain.filter(exchange);
        };
    }

    public static class Config {
    }
}
