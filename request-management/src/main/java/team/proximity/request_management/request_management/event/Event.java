package team.proximity.request_management.request_management.event;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.proximity.request_management.request_management.quotes.QuoteStatus;
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long eventId;
    private String title;
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private String description;
    private String createdBy;

}
