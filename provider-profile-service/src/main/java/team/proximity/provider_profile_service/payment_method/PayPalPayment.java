package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "paypal_payment")
public class PayPalPayment extends PaymentMethod {
    private String firstName;
    private String lastName;
    private String email;
}
