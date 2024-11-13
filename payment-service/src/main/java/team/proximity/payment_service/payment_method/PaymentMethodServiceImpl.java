package team.proximity.payment_service.payment_method;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.payment_service.exceptions.PaymentMethodAlreadyExistException;
import team.proximity.payment_service.exceptions.PaymentPreferenceDoesNotExist;
import team.proximity.payment_service.payment_preference.PaymentPreference;
import team.proximity.payment_service.payment_preference.PaymentPreferenceRepository;

@Service
@Transactional
public class PaymentMethodServiceImpl implements PaymentMethodService {


    private final PaymentMethodFactory paymentMethodFactory;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentPreferenceRepository paymentPreferenceRepository;

    public PaymentMethodServiceImpl(PaymentMethodFactory paymentMethodFactory, PaymentMethodRepository paymentMethodRepository, PaymentPreferenceRepository paymentPreferenceRepository) {
        this.paymentMethodFactory = paymentMethodFactory;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentPreferenceRepository = paymentPreferenceRepository;
    }

    public void createOnePaymentMethod(PaymentMethodRequest request) {
        PaymentPreference paymentPreference = paymentPreferenceRepository.findByName(request.paymentPreference())
                .orElseThrow(() -> new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference()));

        if(paymentMethodRepository.findByAccountName(request.accountName()).isPresent()){
            throw new PaymentMethodAlreadyExistException("Payment method with name " + request.accountName() + "already exist");
        }
        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);

        paymentMethodRepository.save(paymentMethod);
    }

}
