package team.proximity.provider_profile_service.validations;

import org.springframework.stereotype.Component;
import team.proximity.service_provider_profile.about_your_business.about.AboutRepository;
import team.proximity.service_provider_profile.about_your_business.about.AboutRequest;
import team.proximity.service_provider_profile.about_your_business.exception.AboutAlreadyExistsException;

@Component
public class AboutValidator implements Validator<AboutRequest> {

    private final AboutRepository aboutRepository;


    public AboutValidator(AboutRepository aboutRepository) {
        this.aboutRepository = aboutRepository;
    }


    public void validate(AboutRequest aboutRequest) {
        if (aboutRepository.findByInceptionDate(aboutRequest.inceptionDate()).isPresent()) {
            throw new AboutAlreadyExistsException("An About entry with this inception date already exists.");
        }
    }
}
