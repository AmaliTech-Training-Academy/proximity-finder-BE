package team.proximity.management.validators.upload;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;

public class ImageValidationStrategy implements FileValidationStrategy {

    @Override
    public void validate(MultipartFile file) throws InvalidFileTypeException {
        String contentType = file.getContentType();
        if (contentType == null ||
                (!contentType.equals("image/jpeg") &&
                        !contentType.equals("image/png") &&
                        !contentType.equals("image/jpg") &&
                        !contentType.equals("image/gif"))) {
            throw new InvalidFileTypeException("Invalid file type. Only image files are allowed.");
        }
    }
}
