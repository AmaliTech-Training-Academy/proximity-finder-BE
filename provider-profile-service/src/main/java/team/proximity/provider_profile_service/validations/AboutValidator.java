package team.proximity.provider_profile_service.validations;

import io.micrometer.core.instrument.config.validate.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.about.AboutRepository;
import team.proximity.provider_profile_service.about.AboutRequest;
import team.proximity.provider_profile_service.exception.about.AboutAlreadyExistsException;
import team.proximity.provider_profile_service.exception.about.FileValidationException;


@Component
public class AboutValidator{

    private static final Logger LOGGER = LoggerFactory.getLogger(AboutValidator.class);

    private final FileValidator fileValidator;

    public AboutValidator(FileValidator fileValidator) {
        this.fileValidator = fileValidator;
    }


    public void validateRequestFiles(AboutRequest aboutRequest) {
        try {
            fileValidator.validate(aboutRequest.businessIdentityCard());
            fileValidator.validate(aboutRequest.businessCertificate());
        } catch (ValidationException ex) {
            LOGGER.error("Validation failed: {}", ex.getMessage());
            throw new FileValidationException("Validation failed: " + ex.getMessage());
        }
    }
}
