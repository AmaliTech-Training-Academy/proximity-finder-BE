package team.proximity.management.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

@Data
public class ProviderServiceRequest {
    private UUID id;
    @NotNull(message = "Service must be specified")
    private String serviceName;
    @NotBlank(message = "Payment preference must be specified")
    private String paymentPreference;
    @NotBlank(message = "Location must be specified")
    private String location;

    private String schedulingPolicy;
    @NotNull(message = "Booking days must be specified")
    private String bookingDays;
    private List<MultipartFile> documents;
}


