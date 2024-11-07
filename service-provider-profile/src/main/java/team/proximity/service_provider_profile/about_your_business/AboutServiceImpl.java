package team.proximity.service_provider_profile.about_your_business;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
public class AboutServiceImpl implements AboutService {
    private final AboutRepository aboutRepository;
    private final FileStorageService fileStorageService;

    public AboutServiceImpl(AboutRepository aboutRepository, FileStorageService fileStorageService) {
        this.aboutRepository = aboutRepository;
        this.fileStorageService = fileStorageService;
    }



    public void createOneAbout(AboutRequest aboutRequest, MultipartFile businessIdentityCardFile, MultipartFile businessCertificateFile) throws IOException {
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
