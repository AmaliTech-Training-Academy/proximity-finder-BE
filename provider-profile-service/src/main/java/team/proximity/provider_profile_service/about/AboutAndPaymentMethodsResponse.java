package team.proximity.provider_profile_service.about;

import team.proximity.provider_profile_service.payment_method.PaymentMethodResponse;

import java.util.List;

public record AboutAndPaymentMethodsResponse(
        AboutBusinessResponse aboutBusinessResponse,
        List<PaymentMethodResponse> paymentMethodResponses
) {}
