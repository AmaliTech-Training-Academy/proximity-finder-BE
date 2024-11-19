package auth.proximity.authservice.dto;

import org.springframework.web.multipart.MultipartFile;

public record ProfilePictureUpdateRequest(
        MultipartFile file
) {
}
