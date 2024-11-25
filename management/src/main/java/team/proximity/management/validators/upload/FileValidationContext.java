package team.proximity.management.validators.upload;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;

public class FileValidationContext {

    private FileValidationStrategy strategy;

    public FileValidationContext(FileValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setValidationStrategy(FileValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public void validate(MultipartFile file) throws InvalidFileTypeException {
        if (strategy == null) {
            throw new IllegalStateException("No validation strategy set.");
        }
        strategy.validate(file);
    }
}

