package auth.proximity.authservice.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;



public record AdminUpdatePasswordRequest(
         String oldPassword,
         String newPassword,
         String confirmPassword
) {}

