package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mobile_money_payment")
public class MobileMoneyPayment extends PaymentMethod {
    @Enumerated(EnumType.STRING)
    private MobileMoneyServiceProvider serviceProvider;
    private String phoneNumber;
}
