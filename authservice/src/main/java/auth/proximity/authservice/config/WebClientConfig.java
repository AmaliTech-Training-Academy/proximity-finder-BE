package auth.proximity.authservice.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.reactive.function.client.ClientRequest;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;


@Configuration
public class WebClientConfig {

    @Value("${managementservice}")
    private String managementServiceUrl;

    @Value("${providerservice}")
    private String providerServiceUrl;

    @Bean
    public WebClient managementClient(WebClient.Builder builder) {
        return builder.baseUrl(managementServiceUrl).build();
    }

    @Bean
    public WebClient profileServiceClient(WebClient.Builder builder) {
        return builder
                .baseUrl(providerServiceUrl)
                .filter(addAuthorizationHeaderFromRequestContext())
                .build();
    }

    private ExchangeFilterFunction addAuthorizationHeaderFromRequestContext() {
        return (request, next) -> {
            String authHeader = getAuthorizationHeaderFromRequest();
            if (authHeader != null) {
                return next.exchange(
                        ClientRequest.from(request)
                                .headers(headers -> headers.set("Authorization", authHeader))
                                .build()
                );
            }
            return next.exchange(request);
        };
    }

    private String getAuthorizationHeaderFromRequest() {
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        if (requestAttributes instanceof ServletRequestAttributes) {
            HttpServletRequest servletRequest = ((ServletRequestAttributes) requestAttributes).getRequest();
            return servletRequest.getHeader("Authorization");
        }
        return null;
    }
}
