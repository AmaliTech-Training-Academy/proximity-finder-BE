package team.proximity.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ServiceExperience {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String projectTitle;
    private String description;

    @ElementCollection
    @CollectionTable(name = "service_experience_images", joinColumns = @JoinColumn(name = "service_experience_id"))
    @Column(name = "image_urls")
    private List<String> images;

    @OneToOne
    @JoinColumn(name = "provider_service_id", nullable = false)
    @JsonIgnore
    private ProviderService providerService;
}
