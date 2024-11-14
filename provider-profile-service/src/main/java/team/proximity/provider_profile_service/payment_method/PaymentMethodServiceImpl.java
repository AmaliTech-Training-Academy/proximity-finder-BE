package team.proximity.provider_profile_service.payment_method;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.payment_method.PaymentMethodAlreadyExistException;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceDoesNotExist;
import team.proximity.provider_profile_service.payment_preference.PaymentPreference;
import team.proximity.provider_profile_service.payment_preference.PaymentPreferenceRepository;


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

    public void createAnotherPaymentMethod(PaymentMethodRequest request) {

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByName(request.paymentPreference())
                .orElseThrow(() -> new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference()));

        if (paymentMethodRepository.findByCreatedByAndPaymentPreference(AuthHelper.getAuthenticatedUsername(),paymentPreference).isPresent()){
            throw new PaymentMethodAlreadyExistException("Payment method already exist");
        }

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(AuthHelper.getAuthenticatedUsername());

        paymentMethodRepository.save(paymentMethod);
    }

    public void createNewPaymentMethod(PaymentMethodRequest request) {

        PaymentPreference paymentPreference = paymentPreferenceRepository.findByName(request.paymentPreference())
                .orElseThrow(() -> new PaymentPreferenceDoesNotExist("Payment Preference not found with name: " + request.paymentPreference()));

            paymentMethodRepository.findByCreatedByAndPaymentPreference(AuthHelper.getAuthenticatedUsername(), paymentPreference).
                    ifPresent(paymentMethodRepository::delete);

        PaymentMethod paymentMethod = paymentMethodFactory.createPaymentMethod(request);
        paymentMethod.setPaymentPreference(paymentPreference);
        paymentMethod.setCreatedBy(AuthHelper.getAuthenticatedUsername());

        paymentMethodRepository.save(paymentMethod);
    }


}
