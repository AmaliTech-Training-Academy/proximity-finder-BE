package team.proximity.management.requests;

import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequest {
    private int rating;
    private String content;
    private boolean isAnonymous;
    private UUID providerServiceId;
    private String authorEmail;
}