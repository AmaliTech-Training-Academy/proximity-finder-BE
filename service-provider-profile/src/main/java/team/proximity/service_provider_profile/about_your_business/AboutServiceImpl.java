package team.proximity.service_provider_profile.about_your_business;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.service_provider_profile.about_your_business.validations.FileValidator;

import java.io.IOException;

@Service
public class AboutServiceImpl implements AboutService {
    private final FileStorageService fileStorageService;
    private final AboutRepository aboutRepository;
    private final FileValidator fileValidator;

    public AboutServiceImpl(FileStorageService fileStorageService, AboutRepository aboutRepository, FileValidator fileValidator) {
        this.fileStorageService = fileStorageService;
        this.aboutRepository = aboutRepository;
        this.fileValidator = fileValidator;
    }


    public void createOneAbout(AboutRequest aboutRequest, MultipartFile businessIdentityCardFile, MultipartFile businessCertificateFile) throws IOException {
        fileValidator.validateFile(businessIdentityCardFile);
        fileValidator.validateFile(businessCertificateFile);
        String businessIdentityCardUrl = fileStorageService.uploadFile(businessIdentityCardFile);
        String businessCertificateUrl = fileStorageService.uploadFile(businessCertificateFile);

        About about = new About();
        about.setInceptionDate(aboutRequest.inceptionDate());
        about.setSocialMediaLinks(aboutRequest.socialMediaLinks());
        about.setNumberOfEmployees(aboutRequest.numberOfEmployees());
        about.setBusinessIdentityCard(businessIdentityCardUrl);
        about.setBusinessCertificate(businessCertificateUrl);
        about.setBusinessSummary(aboutRequest.businessSummary());
        aboutRepository.save(about);
    }
}
