package team.proximity.provider_profile_service.payment_preference;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Tag(name = "Payment Preferences", description = "Operations related to payment preferences")
@RestController
@RequestMapping("/api/v1/provider-service/payment-preferences")
public class PaymentPreferenceController {
    private final PaymentPreferenceService paymentPreferenceService;

    public PaymentPreferenceController(PaymentPreferenceService paymentPreferenceService) {
        this.paymentPreferenceService = paymentPreferenceService;
    }

    @Operation(summary = "Get a list of all accepted payment preferences")
    @GetMapping
    public ResponseEntity<List<PaymentPreferenceResponse>> getAllPaymentPreferences() {
        Optional<List<PaymentPreferenceResponse>> paymentPreferences = paymentPreferenceService.getAllPaymentPreferences();
        return paymentPreferences
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
