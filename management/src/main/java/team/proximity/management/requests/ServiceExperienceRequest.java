package team.proximity.management.requests;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Data
public class ServiceExperienceRequest {
    @NotBlank
    private UUID providerServiceId;
    @NotBlank(message = "Project title must be specified")
    private String projectTitle;
    @NotBlank(message = "Description must be specified")
    private String description;
    @NotNull(message = "Images must be specified")
    private List<MultipartFile> images;
}

