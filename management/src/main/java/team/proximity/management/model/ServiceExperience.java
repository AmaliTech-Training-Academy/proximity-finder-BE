package team.proximity.management.model;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Entity
@Data
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
}
