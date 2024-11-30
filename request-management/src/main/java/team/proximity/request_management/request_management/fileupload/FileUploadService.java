package team.proximity.request_management.request_management.fileupload;

import org.springframework.web.multipart.MultipartFile;

public interface FileUploadService {
    String uploadFile(MultipartFile file);

}
