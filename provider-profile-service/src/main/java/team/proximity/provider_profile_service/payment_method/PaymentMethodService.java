package team.proximity.provider_profile_service.payment_method;



public interface PaymentMethodService {
    void createAnotherPaymentMethod(PaymentMethodRequest request);
    void createNewPaymentMethod(PaymentMethodRequest request);
}
