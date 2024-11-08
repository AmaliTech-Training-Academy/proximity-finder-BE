package team.proximity.management.dto;


import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ServicesDTO {
    private String name;
    private String description;
    private String category;
    private MultipartFile image;
}
