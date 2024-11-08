package team.proximity.management.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
@AllArgsConstructor
public class ServiceRequest {
    @NotBlank(message = "Name must be specified")
    private String name;
    @NotBlank(message = "Description must be specified")
    private String description;
    @NotBlank(message = "Category must be specified")
    private String category;
    @NotNull(message = "Image must be specified")
    private MultipartFile image;
}