package team.proximity.provider_profile_service.payment_preference;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;


@RestController
@RequestMapping("/api/v1/provider-service/payment-preferences")
public class PaymentPreferenceController {
    private final PaymentPreferenceService paymentPreferenceService;

    public PaymentPreferenceController(PaymentPreferenceService paymentPreferenceService) {
        this.paymentPreferenceService = paymentPreferenceService;
    }


    @GetMapping
    public ResponseEntity<List<PaymentPreferenceResponse>> getAllPaymentPreferences() {
        Optional<List<PaymentPreferenceResponse>> paymentPreferences = paymentPreferenceService.getAllPaymentPreferences();
        return paymentPreferences
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
