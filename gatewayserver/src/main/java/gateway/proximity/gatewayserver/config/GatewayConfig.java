package gateway.proximity.gatewayserver.config;


import gateway.proximity.gatewayserver.filter.AuthenticationFilter;
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
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
        return builder.routes()

                .route("provider-profile-service", r -> r.path("/api/v1/provider-service/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://SERVICE-PROVIDER-PROFILE"))


                .route("quest-and-call-service", r -> r.path("/api/v1/quote-service/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://REQUEST-MANAGEMENT-SERVICE"))

                .route("quest-and-call-service", r -> r.path("/api/v1/support/**")
                        .filters(f -> f.filter(authFilter.apply(new AuthenticationFilter.Config())))
                        .uri("lb://HELP-AND-SUPPORT"))

                .build();
    }
}
