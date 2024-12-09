package team.proximity.request_management.request_management.fileupload;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.request_management.request_management.exception.FileProcessingException;
import team.proximity.request_management.request_management.exception.FileUploadException;
import team.proximity.request_management.request_management.validations.FileValidator;

@Service
public class FileProcessingService {

    private final Logger LOGGER = LoggerFactory.getLogger(FileProcessingService.class);

    private final FileValidator fileValidator;
    private final FileUploadService fileUploadService;

    public FileProcessingService(FileValidator fileValidator, FileUploadService fileUploadService) {
        this.fileValidator = fileValidator;
        this.fileUploadService = fileUploadService;
    }


    public String processImage(MultipartFile file) {
        try {
            fileValidator.validate(file);
        } catch (IllegalArgumentException ex) {
            LOGGER.error("Validation failed for file: {}. Reason: {}", file.getOriginalFilename(), ex.getMessage());
            throw new FileProcessingException("File validation failed: " + ex.getMessage(), ex);
        }
        try {
            return fileUploadService.uploadFile(file);
        } catch (FileUploadException ex) {
            LOGGER.error("File upload failed for file: {}. Reason: {}", file.getOriginalFilename(), ex.getMessage());
            throw new FileProcessingException("File upload failed: " + ex.getMessage(), ex);
        }
    }
}

