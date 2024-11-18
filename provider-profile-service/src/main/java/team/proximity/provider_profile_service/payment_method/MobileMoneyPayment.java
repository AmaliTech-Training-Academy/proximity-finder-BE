package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DiscriminatorValue("MOBILE_MONEY")
public class MobileMoneyPayment extends PaymentMethod {

    @Enumerated(EnumType.STRING)
    private MobileMoneyServiceProvider serviceProvider;
    private String phoneNumber;
}
