package team.proximity.payment_service.payment_method;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("MOBILE_MONEY")
public class MobileMoneyPayment extends PaymentMethod {


    private String serviceProvider;


    private String mobileNumber;


    public MobileMoneyPayment(String serviceProvider, String mobileNumber) {
        this.serviceProvider = serviceProvider;
        this.mobileNumber = mobileNumber;
    }

    public MobileMoneyPayment() {
    }

    public String getServiceProvider() {
        return this.serviceProvider;
    }

    public String getMobileNumber() {
        return this.mobileNumber;
    }

    public void setServiceProvider(String serviceProvider) {
        this.serviceProvider = serviceProvider;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }
}
