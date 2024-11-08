package team.proximity.management.model;

import jakarta.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Entity
public class BookingDay {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @ManyToOne
    @JoinColumn(name = "preference_id")
    private Preference preference;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;

    private LocalTime fromTime;
    private LocalTime toTime;


}

