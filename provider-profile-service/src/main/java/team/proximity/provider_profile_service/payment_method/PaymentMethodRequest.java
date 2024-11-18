package team.proximity.provider_profile_service.payment_method;


import io.swagger.v3.oas.annotations.media.Schema;

public record PaymentMethodRequest(

        String paymentPreference,

        @Schema(description = "The name of the bank, required if payment preference is Bank Account", example = "Fidelity Bank")
        String bankName,

        @Schema(description = "The account number, required for all payment methods", example = "123456789")
        String accountNumber,

        @Schema(description = "The account name, required for all payment methods", example = "John Doe")
        String accountName,

        @Schema(description = "An alias for the account, optional", example = "JD123")
        String accountAlias,

        @Schema(description = "The service provider, required if payment preference is Mobile Money", example = "MTN")
        String serviceProvider,
        @Schema(description = "Phone Number, required if payment preference is Mobile Money")
        String phoneNumber
) {}

