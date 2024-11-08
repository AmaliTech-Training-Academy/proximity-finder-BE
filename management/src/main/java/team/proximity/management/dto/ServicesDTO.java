package team.proximity.management.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ServicesDTO {
    @NotBlank(message = "Name must be specified")
    private String name;
    @NotBlank(message = "Description must be specified")
    private String description;
    @NotBlank(message = "Category must be specified")
    private String category;
    @NotNull(message = "Image must be specified")
    private MultipartFile image;
}
