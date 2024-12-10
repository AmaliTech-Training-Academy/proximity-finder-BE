package gateway.proximity.gatewayserver.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class FallbackController {

    @GetMapping("/fallback/provider-profile-service")
    public ResponseEntity<FallbackResponse> providerProfileFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new FallbackResponse("Provider Profile Service is currently unavailable. Please try again later."));
    }

    @GetMapping("/fallback/quote-service")
    public ResponseEntity<FallbackResponse> quoteServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new FallbackResponse("Quote Service is currently unavailable. Please try again later."));
    }

    @GetMapping("/fallback/support-service")
    public ResponseEntity<FallbackResponse> supportServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new FallbackResponse("Support Service is currently unavailable. Please try again later."));
    }

    @GetMapping("/fallback/management-service")
    public ResponseEntity<FallbackResponse> managementServiceFallback() {
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(new FallbackResponse("Management Service is currently unavailable. Please try again later."));
    }
}

