package team.proximity.request_management.request_management.quotes;
import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import team.proximity.request_management.request_management.descision.QuoteDecision;
import team.proximity.request_management.request_management.request.Request;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quotes")
public class Quote {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long quoteId;
    private String title;
    private String description;
    private String location;
    private String additionalDetails;
    @Enumerated(EnumType.STRING)
    private QuoteStatus status;
    private String startDate;
    private String startTime;
    private String endDate;
    private String endTime;
    private String createdBy;
    private String assignedProvider;
    @OneToOne(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private QuoteDecision decision;
    @OneToMany(mappedBy = "quote", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuoteImage> images = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "request_id", referencedColumnName = "requestId")
    private Request request;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "MM/dd/yyyy")
    private final LocalDate requestDate = LocalDate.now();

}

