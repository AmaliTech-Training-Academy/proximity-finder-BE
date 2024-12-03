package team.proximity.management.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;
import team.proximity.management.validators.upload.FileValidationContext;
import team.proximity.management.validators.upload.FileValidationStrategy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${app.awsServices.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    public Map<String, String> uploadFile(MultipartFile file, FileValidationStrategy validationStrategy) throws IOException {
        // Validate file
        FileValidationContext validationContext = new FileValidationContext(validationStrategy);
        validationContext.validate(file);

        // Convert MultipartFile to File
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Upload to S3
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));

        // Delete temporary file
        fileObj.delete();

        String fileUrl = String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
        Map<String, String> result = new HashMap<>();
        result.put("url", fileUrl);
        result.put("filename", fileName);

        return result;
    }


    private File convertMultiPartFileToFile(MultipartFile file) throws IOException {
        File convertedFile = new File(file.getOriginalFilename());
        try (FileOutputStream fos = new FileOutputStream(convertedFile)) {
            fos.write(file.getBytes());
        }
        return convertedFile;
    }



    public S3Object downloadFile(String fileName) {
        return s3Client.getObject(bucketName, fileName);
    }
}

