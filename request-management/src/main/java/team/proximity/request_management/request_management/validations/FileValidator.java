package team.proximity.request_management.request_management.validations;

import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Arrays;

@Component
public class FileValidator implements Validator<MultipartFile> {


    public void validate(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("File is required");
        }

        String contentType = file.getContentType();
        if (contentType == null || SupportedFileType.fromMineType(contentType).isEmpty()) {
            throw new IllegalArgumentException("Invalid file type. Allowed types: "
                    + Arrays.toString(SupportedFileType.values()));
        }
    }
}
