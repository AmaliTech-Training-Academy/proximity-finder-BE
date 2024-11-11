package team.proximity.management.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

@Data
public class PreferenceRequest {
    @NotNull(message = "User must be specified")
    private UUID userId;
    @NotNull(message = "Service must be specified")
    private UUID serviceId;
    @NotBlank(message = "Payment preference must be specified")
    private String paymentPreference;
    @NotBlank(message = "Location must be specified")
    private String location;
    @NotNull(message = "Scheduling policy must be specified")
    private Boolean sameLocation;
    @NotBlank(message = "Scheduling policy must be specified")
    private String schedulingPolicy;
    @NotNull(message = "Booking days must be specified")
    private String bookingDays;
    private List<MultipartFile> documents;
}


