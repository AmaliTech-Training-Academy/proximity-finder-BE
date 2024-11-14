package team.proximity.provider_profile_service.payment_method;


public record PaymentMethodRequest(
        String paymentPreference,
        String bankName,
        String accountNumber,
        String serviceProvider,
        String mobileNumber,
        String accountName,
        String accountAlias
){}
