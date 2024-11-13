package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.provider_profile_service.exception.payment_method.FileTypeNotSupportedException;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileValidator implements Validator<MultipartFile> {

    private final Set<String> allowedExtensions;


    public FileValidator() {
        this.allowedExtensions = Stream.of(SupportedFileType.JPEG, SupportedFileType.PNG, SupportedFileType.PDF)
                .map(SupportedFileType::getFileExtension)
                .collect(Collectors.toSet());
    }

    public void validate(MultipartFile input) {
        String contentType = input.getContentType();
        if (contentType == null || !allowedExtensions.contains(contentType)) {
            throw new FileTypeNotSupportedException("Invalid file type. Allowed types: " + allowedExtensions);
        }
    }
}
