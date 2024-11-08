package team.proximity.management.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
public class Document {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "preference_id", nullable = false)
    private Preference preference;

    private String fileName;
    private String url;
    private LocalDateTime uploadedAt;

    // Getters and Setters
}
