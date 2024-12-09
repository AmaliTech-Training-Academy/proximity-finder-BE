package team.proximity.management.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

import java.util.UUID;

@Data
public class ReviewRequest {
    @Max(5)
    @Min(1)
    private int rating;
    @NotEmpty(message = "Content cannot be empty")
    private String content;
    private boolean isAnonymous;
    private boolean isPublic;
    private UUID providerServiceId;
//    @Email(message = "Author email should be valid")
//    private String authorEmail;

}