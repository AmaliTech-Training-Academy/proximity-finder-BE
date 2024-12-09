package auth.proximity.authservice.dto.user;

import auth.proximity.authservice.dto.RequestRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data @AllArgsConstructor
@Schema(
        name = "Users",
        description = "Schema to hold User Information"
)
public class UserDto {


    @Size(min = 2, max = 120, message = "The length of the user name should be between 2 and 120")
    @Schema(
            description = "Name of the user", example = "Michael Samuel Ahmed"
    )
    private String userName;

    @NotEmpty(message = "User Email can not be a null or empty")
    @Email
    @Schema(
            description = "Email of the user", example = "seeker@amalitech.org"
    )
    private String email;


    @Pattern(regexp="(^\\+?[1-9]\\d{1,3}[-\\s]?(\\(?\\d{1,4}\\)?[-\\s]?)?\\d{6,10}$)", message = "Mobile Number must be 10 digits")
    @Schema(
            description = "Mobile Number of the user", example = "0209187470"
    )
    private String mobileNumber;

    @NotEmpty(message = "Password can not be a null or empty")
    @Size(min = 8, max = 120, message = "The length of the user password should be between 8 and 120")
    @Schema(
            description = "Password of the user", example = "test@1234"
    )
    @Size(min = 12, message = "Password must be at least 12 characters long")
    @Pattern(
            regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$",
            message = "Password must contain at least one uppercase letter, one lowercase letter, one digit, and one special character."
    )
    private String password;

    @Schema(
            description = "Business Owner Name of the user", example = "AmaliTech"
    )
    private String businessOwnerName;

    @NotNull(message = "Role can not be  null or empty")
    @Schema(
            description = "Role of the user", example = "seeker"
    )
    private RequestRole role;

    private String placeName;
    private double longitude;
    private double latitude;

}