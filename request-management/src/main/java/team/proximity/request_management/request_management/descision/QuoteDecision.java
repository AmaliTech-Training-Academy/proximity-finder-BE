package team.proximity.request_management.request_management.descision;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import team.proximity.request_management.request_management.quotes.Quote;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "quote_decision_details")
public class QuoteDecision {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne
    @JoinColumn(name = "quote_id", referencedColumnName = "quoteId")
    private Quote quote;

    private BigDecimal price;
    private String approvalDetails;
    private String declineReason;
}