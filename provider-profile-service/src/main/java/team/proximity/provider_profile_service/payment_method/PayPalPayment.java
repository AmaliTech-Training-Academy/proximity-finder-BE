package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import team.proximity.payment_service.payment_method.PaymentMethod;

@Entity
@DiscriminatorValue("PAYPAL")
public class PayPalPayment extends PaymentMethod {


    private String accountNumber;


    public PayPalPayment(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public PayPalPayment() {
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
