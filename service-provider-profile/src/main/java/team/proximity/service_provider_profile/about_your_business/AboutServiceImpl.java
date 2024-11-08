package team.proximity.service_provider_profile.about_your_business;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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

    public void createOneAbout(AboutRequest aboutRequest, MultipartFile businessIdentityCardFile, MultipartFile businessCertificateFile) throws IOException {
        aboutValidator.validate(aboutRequest);
        fileValidator.validate(businessIdentityCardFile);
        fileValidator.validate(businessCertificateFile);

        String businessIdentityCardUrl = fileStorageService.uploadFile(businessIdentityCardFile);
        String businessCertificateUrl = fileStorageService.uploadFile(businessCertificateFile);

        About about = About.builder()
                .inceptionDate(aboutRequest.inceptionDate())
                .socialMediaLinks(aboutRequest.socialMediaLinks())
                .numberOfEmployees(aboutRequest.numberOfEmployees())
                .businessIdentityCard(businessIdentityCardUrl)
                .businessCertificate(businessCertificateUrl)
                .businessSummary(aboutRequest.businessSummary())
                .build();
        aboutRepository.save(about);
    }
}
