package team.proximity.provider_profile_service.about;

import org.springframework.stereotype.Component;

@Component
public class AboutBusinessMapper {

    public AboutBusinessResponse mapToResponse(About about) {
        return new AboutBusinessResponse(
                about.getBusinessId(),
                about.getInceptionDate(),
                about.getSocialMediaLinks(),
                about.getBusinessIdentityCard(),
                about.getBusinessCertificate(),
                about.getNumberOfEmployees(),
                about.getBusinessSummary()
        );
    }
}
