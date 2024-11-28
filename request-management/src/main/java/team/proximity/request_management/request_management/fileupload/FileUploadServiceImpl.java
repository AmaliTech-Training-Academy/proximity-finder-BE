package team.proximity.request_management.request_management.fileupload;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import team.proximity.request_management.request_management.exception.FileUploadException;

import java.io.IOException;
import java.util.UUID;

@Service
public class FileUploadServiceImpl implements FileUploadService {

    private  final Logger LOGGER = LoggerFactory.getLogger(FileUploadServiceImpl.class);

    private final S3Client s3Client;

    @Value("${aws.s3.bucketName}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public FileUploadServiceImpl(S3Client s3Client) {
        this.s3Client = s3Client;
    }

    @Override
    public String uploadFile(MultipartFile file) {
        String fileName = generateFileName(file);

        try {
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(fileName)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(putObjectRequest, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            String fileUrl = "https://" + bucketName + ".s3." + region + ".amazonaws.com/" + fileName;
            LOGGER.info("File successfully uploaded: {}", fileUrl);
            return fileUrl;
        } catch (IOException e) {
            String errorMessage = "Failed to upload file: " + file.getOriginalFilename();
            LOGGER.error(errorMessage, e);
            throw new FileUploadException(errorMessage, e);
        } catch (Exception e) {
            String errorMessage = "Unexpected error during file upload for: " + file.getOriginalFilename();
            LOGGER.error(errorMessage, e);
            throw new FileUploadException(errorMessage, e);
        }
    }
    private String generateFileName(MultipartFile file) {
        return UUID.randomUUID() + "_" + file.getOriginalFilename();
    }
}
