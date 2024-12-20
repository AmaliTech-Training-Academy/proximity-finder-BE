package team.proximity.provider_profile_service.about;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long businessId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy")
    private LocalDate inceptionDate;
    private Integer numberOfEmployees;
    @ElementCollection
    @CollectionTable(name = "social_media_links", joinColumns = @JoinColumn(name = "business_id"))
    @Column(name = "link")
    private Set<String> socialMediaLinks;
    private String businessIdentityCard;
    private String businessCertificate;
    private String businessSummary;
    private String createdBy;
}
