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
    private final FileUploadService fileUploadService;
    private final AboutRepository aboutRepository;
    private final FileValidator fileValidator;
    private final AboutValidator aboutValidator;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentMethodMapper paymentMethodMapper;

    public AboutServiceImpl(AboutBusinessMapper aboutBusinessMapper, FileUploadService fileUploadService, AboutRepository aboutRepository, FileValidator fileValidator, AboutValidator aboutValidator, PaymentMethodRepository paymentMethodRepository, PaymentMethodMapper paymentMethodMapper) {
        this.aboutBusinessMapper = aboutBusinessMapper;
        this.fileUploadService = fileUploadService;
        this.aboutRepository = aboutRepository;
        this.fileValidator = fileValidator;
        this.aboutValidator = aboutValidator;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentMethodMapper = paymentMethodMapper;
    }

    public AboutBusinessResponse getAboutForAuthenticatedUser() {
        String authenticatedUsername = AuthHelper.getAuthenticatedUsername();

        if (authenticatedUsername == null) {
            throw new UnauthorizedAccessException("User is not authenticated.");
        }
        About about = aboutRepository.findByCreatedBy(authenticatedUsername)
                .orElseThrow(() -> new AboutNotFoundException("No business found for the authenticated user."));
        return aboutBusinessMapper.mapToResponse(about);
    }


    @Transactional
    public void createOneAbout(AboutRequest aboutRequest) {
        validateRequestFiles(aboutRequest);
        LOGGER.info("Processing About record for user: {}", AuthHelper.getAuthenticatedUsername());

        aboutRepository.findByCreatedBy(AuthHelper.getAuthenticatedUsername())
                .ifPresent(about -> {
                    LOGGER.info("Deleting existing About record for user: {}", AuthHelper.getAuthenticatedUsername());
                    aboutRepository.delete(about);
                });
        String businessIdentityCardPath = fileUploadService.uploadFile(aboutRequest.businessIdentityCard());
        String businessCertificatePath = fileUploadService.uploadFile(aboutRequest.businessCertificate());

        About about = createAboutFromRequest(aboutRequest, businessIdentityCardPath, businessCertificatePath);
        aboutRepository.save(about);
        LOGGER.info("Successfully created About record for user: {}", AuthHelper.getAuthenticatedUsername());
    }

    private void validateRequestFiles(AboutRequest aboutRequest) {
        try {
            fileValidator.validate(aboutRequest.businessIdentityCard());
            fileValidator.validate(aboutRequest.businessCertificate());
        } catch (ValidationException ex) {
            LOGGER.error("Validation failed: {}", ex.getMessage());
            throw new FileValidationException("validation failed: " + ex.getMessage());
        }
    }

    private static About createAboutFromRequest(AboutRequest aboutRequest, String businessIdentityCardPath, String businessCertificatePath) {
        return About.builder()
                .inceptionDate(aboutRequest.inceptionDate())
                .socialMediaLinks(aboutRequest.socialMediaLinks())
                .numberOfEmployees(aboutRequest.numberOfEmployees())
                .businessIdentityCard(businessIdentityCardPath)
                .businessCertificate(businessCertificatePath)
                .businessSummary(aboutRequest.businessSummary())
                .createdBy(AuthHelper.getAuthenticatedUsername())
                .build();
    }

    public AboutAndPaymentMethodsResponse getAboutAndPaymentMethods(String email) {
        About about = aboutRepository.findByCreatedBy(email).orElseThrow();

        AboutBusinessResponse aboutBusinessResponse = aboutBusinessMapper.mapToResponse(about);


        List<PaymentMethod> paymentMethods = paymentMethodRepository.findByCreatedBy(email);

        List<PaymentMethodResponse> paymentMethodResponses = paymentMethods
                .stream()
                .map(paymentMethodMapper::mapToResponse)
                .toList();

        return new AboutAndPaymentMethodsResponse(aboutBusinessResponse, paymentMethodResponses);

    }
}


