package team.proximity.provider_profile_service.payment_method;

import team.proximity.payment_service.payment_method.PaymentMethodRequest;

public interface PaymentMethodService {
    void createOnePaymentMethod(PaymentMethodRequest request);
}
