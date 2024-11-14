package team.proximity.provider_profile_service.payment_preference;

import org.springframework.stereotype.Service;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceAlreadyExistException;


import java.util.List;
import java.util.Optional;

@Service
public class PaymentPreferenceServiceImpl implements PaymentPreferenceService {
    private final PaymentPreferenceMapper paymentPreferenceMapper;
    private final PaymentPreferenceRepository paymentPreferenceRepository;

    public PaymentPreferenceServiceImpl(PaymentPreferenceMapper paymentPreferenceMapper, PaymentPreferenceRepository paymentPreferenceRepository) {
        this.paymentPreferenceMapper = paymentPreferenceMapper;
        this.paymentPreferenceRepository = paymentPreferenceRepository;
    }

    public void createOnePaymentPreference(PaymentPreferenceRequest paymentPreferenceRequest) {
        if (paymentPreferenceRepository.findByName(paymentPreferenceRequest.name()).isPresent()) {
            throw new PaymentPreferenceAlreadyExistException("Payment preference with name " + paymentPreferenceRequest.name() + " already exist");
        }
        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setName(paymentPreferenceRequest.name());
        paymentPreferenceRepository.save(paymentPreference);

    }

    public Optional<PaymentPreferenceResponse> getOnePaymentPreference(String name) {
        return paymentPreferenceRepository
                .findByName(name)
                .map(paymentPreferenceMapper::mapToPaymentPreferenceResponse);
    }

    public Optional<List<PaymentPreferenceResponse>> getAllPaymentPreferences() {
        List<PaymentPreference> paymentPreferences = paymentPreferenceRepository.findAll();

        List<PaymentPreferenceResponse> paymentPreferenceResponses = paymentPreferences.stream()
                .map(paymentPreferenceMapper::mapToPaymentPreferenceResponse)
                .toList();

        return Optional.of(paymentPreferenceResponses);
    }
}
