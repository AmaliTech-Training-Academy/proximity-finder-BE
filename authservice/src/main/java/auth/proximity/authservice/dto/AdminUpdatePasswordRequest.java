package auth.proximity.authservice.dto;





public record AdminUpdatePasswordRequest(
         String oldPassword,
         String newPassword,
         String confirmPassword
) {}

