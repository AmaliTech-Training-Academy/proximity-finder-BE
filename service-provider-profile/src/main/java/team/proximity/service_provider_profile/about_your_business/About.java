package team.proximity.service_provider_profile.about_your_business;


import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class About {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long businessId;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate inceptionDate;
    @ElementCollection
    private Set<String> socialMediaLinks;
    private Integer numberOfEmployees;
    private String businessIdentityCard;
    private String businessCertificate;
    private String businessSummary;
}
