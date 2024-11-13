package team.proximity.provider_profile_service.about;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import team.proximity.service_provider_profile.about_your_business.storage.FileStorageService;
import team.proximity.service_provider_profile.about_your_business.validations.AboutValidator;
import team.proximity.service_provider_profile.about_your_business.validations.FileValidator;

import java.io.IOException;

@Service
public class AboutServiceImpl implements AboutService {
    private final FileStorageService fileStorageService;
    private final AboutRepository aboutRepository;
    private final FileValidator fileValidator;
    private final AboutValidator aboutValidator;

    public AboutServiceImpl(FileStorageService fileStorageService, AboutRepository aboutRepository, FileValidator fileValidator, AboutValidator aboutValidator) {
        this.fileStorageService = fileStorageService;
        this.aboutRepository = aboutRepository;
        this.fileValidator = fileValidator;
        this.aboutValidator = aboutValidator;
    }

    public void createOneAbout(AboutRequest aboutRequest) throws IOException {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        System.out.println("Authentication: " + authentication.getName());
        aboutValidator.validate(aboutRequest);
        fileValidator.validate(aboutRequest.businessIdentityCardFile());
        fileValidator.validate(aboutRequest.businessCertificateFile());

        String businessIdentityCardUrl = fileStorageService.uploadFile(aboutRequest.businessIdentityCardFile());
        String businessCertificateUrl = fileStorageService.uploadFile(aboutRequest.businessCertificateFile());

        About about = About.builder()
                .inceptionDate(aboutRequest.inceptionDate())
                .socialMediaLinks(aboutRequest.socialMediaLinks())
                .numberOfEmployees(aboutRequest.numberOfEmployees())
                .businessIdentityCard(businessIdentityCardUrl)
                .businessCertificate(businessCertificateUrl)
                .businessSummary(aboutRequest.businessSummary())
                .createdBy(authentication.getName())
                .build();
        aboutRepository.save(about);
    }
}
