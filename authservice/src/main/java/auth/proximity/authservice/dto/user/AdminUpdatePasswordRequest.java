package auth.proximity.authservice.dto.user;


import jakarta.validation.constraints.Pattern;

public record AdminUpdatePasswordRequest(
         String oldPassword,
         @Pattern(
                 regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
                 message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
         )
         String newPassword,
         String confirmPassword
) {}

