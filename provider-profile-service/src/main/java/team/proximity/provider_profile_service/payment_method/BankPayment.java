package team.proximity.provider_profile_service.payment_method;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("BANK")
public class BankPayment extends PaymentMethod {

    private String bankName;


    private String accountNumber;


    public BankPayment(String bankName, String accountNumber) {
        this.bankName = bankName;
        this.accountNumber = accountNumber;
    }

    public BankPayment() {
    }

    public String getBankName() {
        return this.bankName;
    }

    public String getAccountNumber() {
        return this.accountNumber;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }
}
