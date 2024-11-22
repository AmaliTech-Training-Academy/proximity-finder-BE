package auth.proximity.authservice.dto.security;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Setter @Getter
@Schema(
        name = "Login",
        description = "Schema to hold Login Information"
)
public class LoginRequest {

    @NotEmpty(message = "User Email can not be a null or empty")
    @Email
    @Column(name = "email")
    @Schema(
            description = "Email of the user", example = "seeker@amalitech.org"
    )
    private String email;

    @Size(min = 8, message = "Password must be at least 12 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    @Column(name = "password")
    @Schema(
            description = "Password of the user", example = "test@1234"
    )
    private String password;
}
