package team.proximity.payment_service.payment_preference;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.payment_service.common.ApiResponse;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/payment-preferences")
public class PaymentPreferenceController {
    private final PaymentPreferenceService paymentPreferenceService;

    public PaymentPreferenceController(PaymentPreferenceService paymentPreferenceService) {
        this.paymentPreferenceService = paymentPreferenceService;
    }

    @PostMapping
    public ResponseEntity<ApiResponse> createPaymentPreference(@RequestBody PaymentPreferenceRequest paymentPreferenceRequest) {
            paymentPreferenceService.createOnePaymentPreference(paymentPreferenceRequest);
           return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse("Payment Preference added successfully", true));
    }

    @GetMapping("/{name}")
    public ResponseEntity<PaymentPreferenceResponse> getPaymentPreference(@PathVariable String name) {
        Optional<PaymentPreferenceResponse> paymentPreference = paymentPreferenceService.getOnePaymentPreference(name);
        return paymentPreference
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @GetMapping
    public ResponseEntity<List<PaymentPreferenceResponse>> getAllPaymentPreferences() {
        Optional<List<PaymentPreferenceResponse>> paymentPreferences = paymentPreferenceService.getAllPaymentPreferences();
        return paymentPreferences
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NO_CONTENT).build());
    }
}
