package team.proximity.provider_profile_service.payment_method;


import jakarta.persistence.*;
import team.proximity.payment_service.payment_preference.PaymentPreference;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "payment_type", discriminatorType = DiscriminatorType.STRING)
public class PaymentMethod {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "payment_preference_id", nullable = false)
    private PaymentPreference paymentPreference;


    private String accountName;

    private String accountAlias;

    public PaymentMethod(Long id, PaymentPreference paymentPreference, String accountName, String accountAlias) {
        this.id = id;
        this.paymentPreference = paymentPreference;
        this.accountName = accountName;
        this.accountAlias = accountAlias;
    }

    public PaymentMethod() {
    }

    public Long getId() {
        return this.id;
    }

    public PaymentPreference getPaymentPreference() {
        return this.paymentPreference;
    }

    public String getAccountName() {
        return this.accountName;
    }

    public String getAccountAlias() {
        return this.accountAlias;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setPaymentPreference(PaymentPreference paymentPreference) {
        this.paymentPreference = paymentPreference;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public void setAccountAlias(String accountAlias) {
        this.accountAlias = accountAlias;
    }
}
