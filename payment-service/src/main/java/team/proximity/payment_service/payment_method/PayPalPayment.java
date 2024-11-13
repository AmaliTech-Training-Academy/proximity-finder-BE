package team.proximity.payment_service.payment_method;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

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
