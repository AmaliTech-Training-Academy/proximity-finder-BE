package team.proximity.payment_service.payment_method;

import lombok.*;


public record PaymentMethodRequest(
        String paymentPreference,
        String bankName,
        String accountNumber,
        String serviceProvider,
        String mobileNumber,
        String accountName,
        String accountAlias
){}
