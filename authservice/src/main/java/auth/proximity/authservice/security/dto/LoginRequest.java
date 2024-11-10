package auth.proximity.authservice.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
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

    @NotEmpty(message = "Password can not be a null or empty")
    @Size(min = 8, max = 120, message = "The length of the user password should be between 8 and 120")
    @Column(name = "password")
    @Schema(
            description = "Password of the user", example = "test@1234"
    )
    private String password;
}
