package auth.proximity.authservice.dto.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record UserPasswordResetRequest(
        @NotBlank(message = "Password is mandatory")
        @Size(min = 8, message = "Password must be at least 12 characters long")
        @Pattern(
                regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
        )
        String password,
        @NotBlank(message = "Confirm Password is mandatory")
        String confirmPassword
) {}
