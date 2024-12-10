package auth.proximity.authservice.config;


import auth.proximity.authservice.dto.providerService.AboutBusinessResponse;
import auth.proximity.authservice.dto.providerService.AboutPaymentResponse;
import auth.proximity.authservice.dto.providerService.PaymentMethodResponse;
import auth.proximity.authservice.dto.providerService.ProviderServiceDTO;
import auth.proximity.authservice.dto.user.UserInfoResponse;
import auth.proximity.authservice.services.impl.UserServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class PreviewService {

    private final WebClient managementClient;
    private final WebClient profileServiceClient;
    private final UserServiceImpl userService;

    public PreviewService(@Qualifier("managementClient") WebClient managementClient, @Qualifier("profileServiceClient") WebClient profileServiceClient, UserServiceImpl userService) {
        this.managementClient = managementClient;
        this.profileServiceClient = profileServiceClient;
        this.userService = userService;
    }

    public Mono<Map<String, Object>> getAggregatedData(String email) {
        // Request data from microservice 1
        Mono<ProviderServiceDTO[]> providerServiceData = managementClient
                .get()
                .uri("/api/v1/provider-services/email?userEmail=" + email)
                .retrieve()
                .bodyToMono(ProviderServiceDTO[].class);

        Mono<UserInfoResponse> authServiceData = Mono.justOrEmpty(userService.getUserInfo(email));


        Mono<AboutPaymentResponse> aboutBusinessData = profileServiceClient
                .get()
                .uri("/api/v1/provider-service/about/provider-profile?email=" + email)
                .retrieve()
                .bodyToMono(AboutPaymentResponse.class);

        return Mono.zip(providerServiceData, authServiceData, aboutBusinessData)
                .map(tuple -> Map.of(
                        "provider-service", tuple.getT1(),
                        "authservice", tuple.getT2(),
                        "business-info", tuple.getT3()
                ));
    }
}