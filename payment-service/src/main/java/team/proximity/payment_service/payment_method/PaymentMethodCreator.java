package team.proximity.payment_service.payment_method;

public interface PaymentMethodCreator {
    PaymentMethod create(PaymentMethodRequest request);
}
