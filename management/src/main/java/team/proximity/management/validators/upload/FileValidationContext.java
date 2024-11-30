package team.proximity.management.validators.upload;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;

import java.util.Objects;

public class FileValidationContext {

    private FileValidationStrategy strategy;

    public FileValidationContext(FileValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public void setValidationStrategy(FileValidationStrategy strategy) {
        this.strategy = strategy;
    }

    public void validate(MultipartFile file) throws InvalidFileTypeException {
        Objects.requireNonNull(strategy, "No validation strategy set.");
        strategy.validate(file);
    }
}

