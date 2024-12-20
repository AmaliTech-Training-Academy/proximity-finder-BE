package team.proximity.provider_profile_service.about;

import org.springframework.stereotype.Component;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.upload.FileUploadService;

@Component
public class AboutBusinessMapper {

    private final FileUploadService fileUploadService;

    public AboutBusinessMapper(FileUploadService fileUploadService) {
        this.fileUploadService = fileUploadService;
    }

    public About mapToAbout(AboutRequest aboutRequest) {
        String businessIdentityCardPath = fileUploadService.uploadFile(aboutRequest.businessIdentityCard());
        String businessCertificatePath = fileUploadService.uploadFile(aboutRequest.businessCertificate());

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

    public AboutBusinessResponse mapToABoutResponse(About about) {
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