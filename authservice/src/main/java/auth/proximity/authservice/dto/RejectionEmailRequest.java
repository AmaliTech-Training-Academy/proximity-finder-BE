package auth.proximity.authservice.dto;

import jakarta.validation.constraints.NotEmpty;

public record RejectionEmailRequest (
    @NotEmpty(message = "Email is required")
    String email,

    @NotEmpty(message = "Reason is required")
    String reason
){

}
