package gateway.proximity.gatewayserver.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

    private final List<String> openApiEndpoints = Arrays.asList(
            "/api/auth/public/sign-in",
            "/api/auth/public/refresh-token",
            "/api/auth/public/create"
    );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));
}
