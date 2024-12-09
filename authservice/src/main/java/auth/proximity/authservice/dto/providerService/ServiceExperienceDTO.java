package auth.proximity.authservice.dto.providerService;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ServiceExperienceDTO {
    private Long id;
    private String projectTitle;
    private String description;
    private List<String> images;
}