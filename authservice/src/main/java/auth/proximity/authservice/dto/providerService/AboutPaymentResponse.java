package auth.proximity.authservice.dto.providerService;

import java.util.List;

public record AboutPaymentResponse(
        AboutBusinessResponse aboutBusinessResponse,
        List<PaymentMethodResponse> paymentMethodResponses
) {
}
