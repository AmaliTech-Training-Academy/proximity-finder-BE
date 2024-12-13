package auth.proximity.authservice.dto.providerService;

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
