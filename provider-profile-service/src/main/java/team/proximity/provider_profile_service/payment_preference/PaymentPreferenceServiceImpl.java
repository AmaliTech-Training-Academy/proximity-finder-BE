package team.proximity.provider_profile_service.payment_preference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.proximity.provider_profile_service.exception.payment_method.PaymentPreferenceAlreadyExistException;


import java.util.List;
import java.util.Optional;

@Service
public class PaymentPreferenceServiceImpl implements PaymentPreferenceService {
    private final Logger LOGGER = LoggerFactory.getLogger(PaymentPreferenceServiceImpl.class);
    private final PaymentPreferenceMapper paymentPreferenceMapper;
    private final PaymentPreferenceRepository paymentPreferenceRepository;

    public PaymentPreferenceServiceImpl(PaymentPreferenceMapper paymentPreferenceMapper, PaymentPreferenceRepository paymentPreferenceRepository) {
        this.paymentPreferenceMapper = paymentPreferenceMapper;
        this.paymentPreferenceRepository = paymentPreferenceRepository;
    }

    public void createOnePaymentPreference(PaymentPreferenceRequest paymentPreferenceRequest) {
        if (paymentPreferenceRepository.findByPreference(paymentPreferenceRequest.paymentPreference()).isPresent()) {
            throw new PaymentPreferenceAlreadyExistException("Payment preference with name " + paymentPreferenceRequest.paymentPreference() + " already exist");
        }
        PaymentPreference paymentPreference = new PaymentPreference();
        paymentPreference.setPreference(paymentPreferenceRequest.paymentPreference());
        paymentPreferenceRepository.save(paymentPreference);

    }

    public Optional<PaymentPreferenceResponse> getOnePaymentPreference(String name) {
        return paymentPreferenceRepository
                .findByPreference(name)
                .map(paymentPreferenceMapper::mapToPaymentPreferenceResponse);
    }

    public Optional<List<PaymentPreferenceResponse>> getAllPaymentPreferences() {
        List<PaymentPreference> paymentPreferences = paymentPreferenceRepository.findAll();

        List<PaymentPreferenceResponse> paymentPreferenceResponses = paymentPreferences.stream()
                .map(paymentPreferenceMapper::mapToPaymentPreferenceResponse)
                .toList();
        LOGGER.info("Retrieving all payment preferences");
        return Optional.of(paymentPreferenceResponses);
    }
}
