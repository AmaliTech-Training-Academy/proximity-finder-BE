package team.proximity.service_provider_profile.about_your_business;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String uploadFile(MultipartFile file) throws IOException;
}