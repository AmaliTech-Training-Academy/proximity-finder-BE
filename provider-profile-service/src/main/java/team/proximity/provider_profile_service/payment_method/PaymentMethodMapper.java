package team.proximity.provider_profile_service.payment_method;

import org.springframework.stereotype.Component;

@Component
public class PaymentMethodMapper {

    public PaymentMethodResponse mapToResponse(PaymentMethod paymentMethod) {
        return switch (paymentMethod) {
            case BankPayment bankPayment -> PaymentMethodResponse.builder()
                    .id(bankPayment.getId())
                    .paymentPreference("Bank Account")
                    .bankName(bankPayment.getBankName())
                    .accountName(bankPayment.getAccountName())
                    .accountAlias(bankPayment.getAccountAlias())
                    .accountNumber(bankPayment.getAccountNumber())
                    .build();
            case MobileMoneyPayment mobileMoneyPayment -> PaymentMethodResponse.builder()
                    .id(mobileMoneyPayment.getId())
                    .paymentPreference("Mobile Money")
                    .serviceProvider(mobileMoneyPayment.getServiceProvider().name())
                    .accountName(mobileMoneyPayment.getAccountName())
                    .accountAlias(mobileMoneyPayment.getAccountAlias())
                    .phoneNumber(mobileMoneyPayment.getPhoneNumber())
                    .build();
            case PayPalPayment payPalPayment -> PaymentMethodResponse.builder()
                    .id(payPalPayment.getId())
                    .paymentPreference("PayPal")
                    .firstName(payPalPayment.getFirstName())
                    .lastName(payPalPayment.getLastName())
                    .email(payPalPayment.getEmail())

                    .build();
            default -> throw new IllegalArgumentException("Unknown PaymentMethod type");
        };
    }
}
