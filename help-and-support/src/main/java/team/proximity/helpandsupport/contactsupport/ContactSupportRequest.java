package team.proximity.helpandsupport.contactsupport;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ContactSupportRequest(

        @NotNull(message = "Email is required")
        @Email(message = "Please provide a valid email address")
        String email,

        @NotNull(message = "Subject is required")
        @Size(min = 1, max = 100, message = "Subject must be between 1 and 100 characters")
        String subject,

        @Size(max = 1000, message = "Message cannot exceed 1000 characters")
        String message

) {
}
