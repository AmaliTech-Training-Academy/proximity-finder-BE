package auth.proximity.authservice.dto;

import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public record ProfilePictureUpdateRequest(
        @NotNull(message = "Profile picture must be specified")
        MultipartFile file
) {
}
