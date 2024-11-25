package gateway.proximity.gatewayserver.config;

import gateway.proximity.gatewayserver.filter.AuthenticationFilter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class GatewayConfig {

    private final AuthenticationFilter authFilter;

    public GatewayConfig(AuthenticationFilter authFilter) {
        this.authFilter = authFilter;
    }

    @Bean
    public RouteLocator providerRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
            .route("provider-profile-service", r -> r.path("/api/v1/**")
                .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                .uri("lb://service-provider-profile"))
            

            .build();
    }

    @Bean
    public RouteLocator managementRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("management-service", r -> r.path("/api/v1/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://management"))

                .build();
    }

}
