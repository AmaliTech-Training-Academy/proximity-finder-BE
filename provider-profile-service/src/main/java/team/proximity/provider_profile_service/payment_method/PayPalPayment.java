package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("PAYPAL")
public class PayPalPayment extends PaymentMethod {

    private String firstName;
    private String lastName;
    private String email;

}
