package team.proximity.management.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class UpdateServiceRequest {

    private String name;
    private String description;
    private MultipartFile image;
}
