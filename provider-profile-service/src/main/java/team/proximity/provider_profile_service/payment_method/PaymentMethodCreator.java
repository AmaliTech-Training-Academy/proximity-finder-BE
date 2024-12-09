package team.proximity.provider_profile_service.payment_method;

public interface PaymentMethodCreator {
    PaymentMethod create(PaymentMethodRequest request);
    PaymentMethod update(PaymentMethod existing, PaymentMethodRequest request);
}
