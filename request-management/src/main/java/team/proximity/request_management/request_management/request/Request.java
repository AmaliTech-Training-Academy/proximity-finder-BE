package team.proximity.request_management.request_management.request;

import jakarta.persistence.*;
import lombok.*;
import team.proximity.request_management.request_management.quotes.Quote;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "quote_request")
public class Request {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long requestId;
    private String clientName;
    private String description;
    private String clientEmail;
    private String requestDate;
    private String assignedProvider;
    @OneToOne(mappedBy = "request", cascade = CascadeType.ALL)
    private Quote quote;
}
