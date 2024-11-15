package team.proximity.provider_profile_service.about;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.about.AboutGlobalExceptionHandler;
import team.proximity.provider_profile_service.storage.FileStorageService;
import team.proximity.provider_profile_service.validations.AboutValidator;
import team.proximity.provider_profile_service.validations.FileValidator;


import java.io.IOException;
import java.util.Optional;

@Service
public class AboutServiceImpl implements AboutService {

    private final Logger LOGGER = LoggerFactory.getLogger(AboutServiceImpl.class);

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

    @Transactional
    public void createOneAbout(AboutRequest aboutRequest) throws IOException {

        LOGGER.info("Processing About record for user: {}", AuthHelper.getAuthenticatedUsername());

        Optional<About> existingAbout = aboutRepository.findByCreatedBy(AuthHelper.getAuthenticatedUsername());
        existingAbout.ifPresent(about -> {

            LOGGER.info("Deleting existing About record for user: {}", AuthHelper.getAuthenticatedUsername());
            aboutRepository.delete(about);
        });


        String businessIdentityCardPath = fileStorageService.uploadFile(aboutRequest.businessIdentityCard());
        LOGGER.info("Uploaded business identity card for user: {}", AuthHelper.getAuthenticatedUsername());

        String businessCertificatePath = fileStorageService.uploadFile(aboutRequest.businessCertificate());
        LOGGER.info("Uploaded business certificate for user: {}", AuthHelper.getAuthenticatedUsername());

        About about = About.builder()
                .inceptionDate(aboutRequest.inceptionDate())
                .socialMediaLinks(aboutRequest.socialMediaLinks())
                .numberOfEmployees(aboutRequest.numberOfEmployees())
                .businessIdentityCard(businessIdentityCardPath)
                .businessCertificate(businessCertificatePath)
                .businessSummary(aboutRequest.businessSummary())
                .createdBy(AuthHelper.getAuthenticatedUsername())
                .build();

        aboutRepository.save(about);

        LOGGER.info("Successfully created About record for user: {}", AuthHelper.getAuthenticatedUsername());
    }
}


