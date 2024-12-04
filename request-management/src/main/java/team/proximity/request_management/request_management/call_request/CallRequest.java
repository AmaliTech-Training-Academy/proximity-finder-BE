package team.proximity.request_management.request_management.call_request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
public class CallRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;
    private String clientName;
    private String phoneNumber;
    private String clientEmail;
    @Enumerated(EnumType.STRING)
    private Status status;
    private String assignedProvider;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MMMM dd, yyyy")
    private final LocalDate requestDate = LocalDate.now();
}
