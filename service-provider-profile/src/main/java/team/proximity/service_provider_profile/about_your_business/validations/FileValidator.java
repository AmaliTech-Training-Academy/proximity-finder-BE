package team.proximity.service_provider_profile.about_your_business;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileValidator {

    private final Set<String> allowedExtensions;


    public FileValidator() {
        this.allowedExtensions = Stream.of(SupportedFileType.JPEG, SupportedFileType.PNG, SupportedFileType.PDF)
                .map(SupportedFileType::getFileExtension)
                .collect(Collectors.toSet());
    }

    public void validateFile(MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        if (contentType == null || !allowedExtensions.contains(contentType)) {
            throw new FileTypeNotSupportedException("Invalid file type. Allowed types: " + allowedExtensions);
        }
    }
}
