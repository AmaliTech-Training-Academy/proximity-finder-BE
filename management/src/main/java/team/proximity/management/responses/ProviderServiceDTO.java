package team.proximity.management.responses;
import lombok.Data;
import team.proximity.management.model.ServiceExperience;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class ProviderServiceDTO {
    private UUID id;
    private String userEmail;
    private String serviceName;
    private String paymentPreference;
    private String placeName;
    private String schedulingPolicy;
    private ServiceExperience serviceExperience;
}
