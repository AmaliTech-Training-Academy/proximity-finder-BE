package team.proximity.request_management.request_management.call_request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record SeekerCallRequest(
        @NotBlank(message = "Phone number cannot be blank")
        @Pattern(regexp = "\\+?[0-9]{10,15}", message = "Phone number must be valid and contain 10-15 digits")
        String phoneNumber,

        @NotBlank(message = "Assigned provider cannot be blank")
        @Size(max = 200, message = "Assigned provider name cannot exceed 200 characters")
        String assignedProvider
){}
