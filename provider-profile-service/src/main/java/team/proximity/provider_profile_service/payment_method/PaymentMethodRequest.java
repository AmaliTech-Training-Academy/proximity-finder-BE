package team.proximity.provider_profile_service.payment_method;


import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

import jakarta.validation.constraints.Pattern;

public record PaymentMethodRequest(

        @NotNull(message = "Payment preference is required.")

        String paymentPreference,

       @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Invalid bank name format.")
       String bankName,

        @Pattern(regexp = "^\\d{13}$", message = "Account number must be exactly 13 digits.")
        String accountNumber,

       @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "Invalid account name format.")
       String accountName,

       @Pattern(regexp = "^[A-Za-z0-9-_]+$", message = "Invalid account alias format.")
       String accountAlias,

       @Pattern(regexp = "^[A-Za-z\\s]+$", message = "Invalid service provider format.")
       String serviceProvider,

       @Pattern(regexp = "^(\\+\\d{1,3}[- ]?)?\\(?\\d{1,4}\\)?[- ]?\\d{1,4}[- ]?\\d{1,4}$", message = "Invalid phone number format.")
       String phoneNumber,

       @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "Invalid first name format.")
       String firstName,

       @Pattern(regexp = "^[A-Za-z\\s'-]+$", message = "Invalid last name format.")
       String lastName,

       @Email(message = "Invalid email format.")
        @Pattern(
                regexp = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$",
                message = "Email must include a valid domain."
        )
       String email

) {}

