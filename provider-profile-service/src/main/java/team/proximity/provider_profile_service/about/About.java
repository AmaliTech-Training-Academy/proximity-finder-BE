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
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long businessId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Column(nullable = false)
    private LocalDate inceptionDate;
    @ElementCollection
    private Set<String> socialMediaLinks;
    private Integer numberOfEmployees;
    @Column(nullable = false)
    private String businessIdentityCard;
    @Column(nullable = false)
    private String businessCertificate;
    @Column(nullable = false)
    private String businessSummary;
    @Column(nullable = false)
    private String createdBy;
}
