package gateway.proximity.gatewayserver.filter;

import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
public class RouteValidator {

        public static final List<String> openApiEndpoints = List.of(


                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                        "/api/v1/provider-service/banks",
                        "api/v1/provider-service/payment-method/providers/mobile-money-providers",
                        "api/v1/provider-service/payment-preferences",
                        "/api/v1/support/faq-groups/",
                        "/actuator/health",
                        "/actuator/info",
                        "/actuator/metrics",
                        "/eureka"
        );

        public Predicate<ServerHttpRequest> isSecured = request -> openApiEndpoints
                        .stream()
                        .noneMatch(uri -> request.getURI().getPath().contains(uri));
}