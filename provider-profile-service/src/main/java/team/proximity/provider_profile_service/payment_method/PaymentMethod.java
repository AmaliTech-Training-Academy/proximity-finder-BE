package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.*;
import lombok.*;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_preference_id", nullable = false)
    private PaymentPreference paymentPreference;

    private String accountName;
    private String accountAlias;
    private String createdBy;
}
