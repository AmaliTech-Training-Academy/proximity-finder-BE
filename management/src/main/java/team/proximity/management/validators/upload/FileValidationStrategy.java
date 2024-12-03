package team.proximity.management.validators.upload;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;

public interface FileValidationStrategy {
    void validate(MultipartFile file) throws InvalidFileTypeException;
}

