package team.proximity.provider_profile_service.payment_method;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;

@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public record PaymentMethodResponse(
        Long id,
        String paymentPreference,
        String accountName,
        String accountAlias,
        String bankName,
        String accountNumber,
        String serviceProvider,
        String phoneNumber,
        String firstName,
        String lastName,
        String email
) {}
