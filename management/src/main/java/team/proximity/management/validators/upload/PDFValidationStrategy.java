package team.proximity.management.validators.upload;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;

public class PDFValidationStrategy implements FileValidationStrategy {

    @Override
    public void validate(MultipartFile file) throws InvalidFileTypeException {
        // Get MIME type of the file
        String contentType = file.getContentType();

        // Check if it's a valid PDF
        if (contentType == null || !contentType.equals("application/pdf")) {
            throw new InvalidFileTypeException("Invalid file type. Only PDF files are allowed.");
        }

        // Additional validation: Ensure file size is within a limit (optional)
        long maxFileSize = 5 * 1024 * 1024; // 5 MB
        if (file.getSize() > maxFileSize) {
            throw new InvalidFileTypeException("PDF file size exceeds the allowed limit of 5 MB.");
        }
    }
}

