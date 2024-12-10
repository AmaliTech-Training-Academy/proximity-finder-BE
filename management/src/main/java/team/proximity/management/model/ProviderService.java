package team.proximity.management.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.locationtech.jts.awt.PointShapeFactory;
import org.locationtech.jts.geom.Point;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProviderService {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @Column(nullable = false)
    private String userEmail;

    @ManyToOne
    @JoinColumn(name = "service_id", nullable = false)
    private Services service;

    private String paymentPreference;

    @JsonIgnore
    @Column(columnDefinition = "geography(Point,4326)")
    private Point location;

    private String placeName;
    @OneToOne(mappedBy = "providerService", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private ServiceExperience serviceExperience;
    private String schedulingPolicy;
    @OneToMany(mappedBy = "preference", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<BookingDay> bookingDays;

    @OneToMany(mappedBy = "preference", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<Document> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
