package team.proximity.provider_profile_service.validations;


import team.proximity.provider_profile_service.payment_method.PaymentMethodRequest;

public interface PaymentMethodValidator {
    void validate(PaymentMethodRequest request);
}

