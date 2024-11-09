package team.proximity.management.requests;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;


import java.util.List;
import java.util.UUID;

@Data
public class PreferenceRequest {
    private UUID userId;
    private UUID serviceId;
    private String paymentPreference;
    private String location;
    private Boolean sameLocation;
    private String schedulingPolicy;
    private List<BookingDayRequest> bookingDays;
    private List<MultipartFile> documents;
}


