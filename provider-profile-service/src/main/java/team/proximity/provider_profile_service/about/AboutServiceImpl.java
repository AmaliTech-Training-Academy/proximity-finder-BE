package team.proximity.provider_profile_service.about;

import io.micrometer.core.instrument.config.validate.ValidationException;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.about.AboutNotFoundException;
import team.proximity.provider_profile_service.exception.about.FileValidationException;
import team.proximity.provider_profile_service.exception.about.UnauthorizedAccessException;
import team.proximity.provider_profile_service.payment_method.PaymentMethod;
import team.proximity.provider_profile_service.payment_method.PaymentMethodMapper;
import team.proximity.provider_profile_service.payment_method.PaymentMethodRepository;
import team.proximity.provider_profile_service.payment_method.PaymentMethodResponse;
import team.proximity.provider_profile_service.upload.FileUploadService;
import team.proximity.provider_profile_service.validations.AboutValidator;
import team.proximity.provider_profile_service.validations.FileValidator;

import java.util.List;


@Service
public class AboutServiceImpl implements AboutService {

    private final Logger LOGGER = LoggerFactory.getLogger(AboutServiceImpl.class);

    private final AboutBusinessMapper aboutBusinessMapper;
    private final AboutRepository aboutRepository;
    private final AboutValidator aboutValidator;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    public AboutServiceImpl(AboutBusinessMapper aboutBusinessMapper, AboutRepository aboutRepository, AboutValidator aboutValidator, PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper) {
        this.aboutBusinessMapper = aboutBusinessMapper;
        this.aboutRepository = aboutRepository;
        this.aboutValidator = aboutValidator;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }


    public AboutBusinessResponse getAboutForAuthenticatedUser() {
        LOGGER.debug("Attempting to retrieve about info for authenticated user");
        String authenticatedUsername = AuthHelper.getAuthenticatedUsername();

        if (authenticatedUsername == null) {
            LOGGER.error("Authentication failed - no username found in context");
            throw new UnauthorizedAccessException("User is not authenticated.");
        }
        LOGGER.debug("Looking up business info for user: {}", authenticatedUsername);
        About about = aboutRepository.findByCreatedBy(authenticatedUsername)
                .orElseThrow(() -> {
                    LOGGER.error("No business found for user: {}", authenticatedUsername);
                    return new AboutNotFoundException("No business found for the authenticated user.");
                });
        LOGGER.info("Successfully retrieved business info for user: {}", authenticatedUsername);
        return aboutBusinessMapper.mapToABoutResponse(about);
    }


    @Transactional
    public void createAbout(AboutRequest aboutRequest) {
        LOGGER.debug("Starting creation of new about info");
        aboutValidator.validateRequestFiles(aboutRequest);
        LOGGER.debug("File validation passed successfully");

        String username = AuthHelper.getAuthenticatedUsername();
        LOGGER.debug("Checking for existing about info for user: {}", username);
        aboutRepository.findByCreatedBy(username)
                .ifPresent(existing -> {
                    LOGGER.info("Deleting existing about info for user: {}", username);
                    aboutRepository.delete(existing);
                });

        About about = aboutBusinessMapper.mapToAbout(aboutRequest);
        aboutRepository.save(about);
        LOGGER.info("Successfully created new about info for user: {}", username);
    }


    public AboutAndPaymentMethodsResponse getAboutAndPaymentMethods(String email) {
        LOGGER.debug("Retrieving about and payment methods for email: {}", email);

        About about = aboutRepository.findByCreatedBy(email)
                .orElseThrow(() -> {
                    LOGGER.error("No about info found for email: {}", email);
                    return new AboutNotFoundException("No business found for email: " + email);
                });

        AboutBusinessResponse aboutBusinessResponse = aboutBusinessMapper.mapToABoutResponse(about);
        LOGGER.debug("Successfully retrieved about info for email: {}", email);

        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByCreatedBy(email);
        LOGGER.debug("Found {} payment methods for email: {}", paymentMethods.size(), email);

        List<PaymentMethodResponse> paymentMethodResponses = paymentMethods
                .stream()
                .map(paymentMethodMapper::mapToResponse)
                .toList();

        LOGGER.info("Successfully retrieved about and payment methods for email: {}", email);
        return new AboutAndPaymentMethodsResponse(aboutBusinessResponse, paymentMethodResponses);
    }
}