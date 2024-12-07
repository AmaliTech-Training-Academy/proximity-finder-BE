package auth.proximity.authservice.dto.providerService;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderServiceDTO {
    private UUID id;
    private String userEmail;
    private String paymentPreference;
    private String placeName;
    private ServiceExperienceDTO serviceExperience;
    private String schedulingPolicy;
    private ServiceDTO service;
    private List<BookingDayDTO> bookingDays;
    private List<DocumentDTO> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}