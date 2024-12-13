package team.proximity.management.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.geom.Point;
import team.proximity.management.dtos.BookingDayDTO;
import team.proximity.management.dtos.DocumentDTO;
import team.proximity.management.dtos.ServiceExperienceDTO;

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
    private Point location;
    private String placeName;
    private ServiceDTO service;
    private ServiceExperienceDTO serviceExperience;
    private String schedulingPolicy;
    private List<BookingDayDTO> bookingDays;
    private List<DocumentDTO> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}