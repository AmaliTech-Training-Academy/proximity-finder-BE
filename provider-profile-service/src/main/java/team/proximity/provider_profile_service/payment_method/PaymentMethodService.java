package team.proximity.provider_profile_service.payment_method;


import java.util.List;

public interface PaymentMethodService {
    void createAnotherPaymentMethod(PaymentMethodRequest request);
    void createNewPaymentMethod(PaymentMethodRequest request);
    List<PaymentMethodResponse> getPaymentMethodsForAuthenticatedUser();
    void updatePaymentMethod(PaymentMethodRequest request, Long paymentMethodId);
    void deletePaymentMethodById(Long paymentMethodId);
}
