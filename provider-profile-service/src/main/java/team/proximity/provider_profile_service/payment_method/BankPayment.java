package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "bank_payment")
public class BankPayment extends PaymentMethod {
    private String bankName;
    private String accountNumber;
}
