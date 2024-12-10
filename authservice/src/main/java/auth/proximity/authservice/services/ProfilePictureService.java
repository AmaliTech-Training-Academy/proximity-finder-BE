package auth.proximity.authservice.services;

import auth.proximity.authservice.dto.ProfilePictureUpdateRequest;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class ProfilePictureService {

    private final S3Service s3Service;
    private final UserRepository userRepository;

    public ProfilePictureService(UserRepository userRepository, S3Service s3Service) {
        this.userRepository = userRepository;
        this.s3Service = s3Service;
    }

    @Transactional
    public String updateProfilePicture(String email, ProfilePictureUpdateRequest profilePictureUpdateRequest) {
        try {
            validateFileType(profilePictureUpdateRequest.file());
            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found with ID: " + email));
            String fileUrl = s3Service.uploadFile(profilePictureUpdateRequest.file());
            user.setProfileImage(fileUrl);
            userRepository.save(user);
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload profile picture", e);
        }
    }


    private void validateFileType(MultipartFile file) {
        String contentType = file.getContentType();
        if (!MediaType.IMAGE_JPEG_VALUE.equals(contentType) && !MediaType.IMAGE_PNG_VALUE.equals(contentType)) {
            throw new IllegalArgumentException("Invalid file type. Only JPEG and PNG files are accepted.");
        }
    }
}
