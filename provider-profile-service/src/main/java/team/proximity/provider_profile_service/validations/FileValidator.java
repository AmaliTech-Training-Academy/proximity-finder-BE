package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.provider_profile_service.exception.payment_method.FileTypeNotSupportedException;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class FileValidator implements Validator<MultipartFile> {


    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || SupportedFileType.fromMineType(contentType).isEmpty()) {
            throw new FileTypeNotSupportedException("Invalid file type. Allowed types: "
                    + Arrays.toString(SupportedFileType.values()));
        }
    }
}
