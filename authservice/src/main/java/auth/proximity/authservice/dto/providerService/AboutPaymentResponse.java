package auth.proximity.authservice.dto.providerService;

public record AboutPaymentResponse(
        AboutBusinessResponse aboutBusinessResponse,
        PaymentMethodResponse paymentMethodResponse
) {
}
