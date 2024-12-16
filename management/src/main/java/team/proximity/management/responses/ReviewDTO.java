package team.proximity.management.responses;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

// ReviewDTO.java
@Data
public class ReviewDTO {
    private Long id;
    private int rating;
    private String content;
    private boolean isAnonymous;
    private UUID providerServiceId;
    private String userEmail; // Only included if not anonymous
    private LocalDateTime createdAt;
    private String sentiment;
}