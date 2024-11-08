package team.proximity.service_provider_profile.about_your_business.validations;

import org.springframework.stereotype.Component;
import team.proximity.service_provider_profile.about_your_business.AboutRepository;
import team.proximity.service_provider_profile.about_your_business.AboutRequest;
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
