package team.proximity.provider_profile_service.payment_method;

import team.proximity.payment_service.payment_method.PaymentMethod;
import team.proximity.payment_service.payment_method.PaymentMethodRequest;

public interface PaymentMethodCreator {
    PaymentMethod create(PaymentMethodRequest request);
}
