package team.proximity.request_management.request_management.booking;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Booking {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long bookingId;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String description;
    private String createdBy;
    private String assignedProvider;
    @Enumerated(EnumType.STRING)
    private BookingStatus status;
}
