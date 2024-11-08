package team.proximity.management.services;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.InvalidFileTypeException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;


@Service
public class S3Service {

    @Autowired
    private AmazonS3 s3Client;

    @Value("${app.awsServices.bucketName}")
    private String bucketName;
    @Value("${cloud.aws.region.static}")
    private String region;

    public String uploadFile(MultipartFile file) throws IOException {
        // Check if the file is an image
        if (!isValidImage(file)) {
            throw new InvalidFileTypeException("Invalid file type. Only image files are allowed.");
        }

        // Convert MultipartFile to File
        File fileObj = convertMultiPartFileToFile(file);
        String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();

        // Upload to S3
        s3Client.putObject(new PutObjectRequest(bucketName, fileName, fileObj));

        // Delete temporary file
        fileObj.delete();

        return String.format("https://%s.s3.%s.amazonaws.com/%s", bucketName, region, fileName);
    }

    private boolean isValidImage(MultipartFile file) throws IOException {
        // Get MIME type
        String contentType = file.getContentType();

        // Allow only common image MIME types
        return contentType.equals("image/jpeg") ||
                contentType.equals("image/png") ||
                contentType.equals("image/jpg") ||
                contentType.equals("image/gif");
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

