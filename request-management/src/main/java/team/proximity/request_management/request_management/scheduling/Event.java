package team.proximity.request_management.request_management.scheduling;

import jakarta.persistence.*;
import jakarta.persistence.GenerationType;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;
    private String title;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String description;
    private String createdBy;

}
