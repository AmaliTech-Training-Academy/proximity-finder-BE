package team.proximity.management.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.UUID;

@Entity
@Data
public class Preference {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;
    private UUID userId;
    private UUID serviceId;
    private String paymentPreference;
    private String location;
    private Boolean sameLocation;

    private String schedulingPolicy;
    @OneToMany(mappedBy = "preference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<BookingDay> bookingDays;
    @OneToMany(mappedBy = "preference", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Document> documents;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

