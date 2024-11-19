package team.proximity.provider_profile_service.about;

import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import team.proximity.provider_profile_service.common.AuthHelper;
import team.proximity.provider_profile_service.exception.about.AboutNotFoundException;
import team.proximity.provider_profile_service.exception.about.UnauthorizedAccessException;
import team.proximity.provider_profile_service.upload.FileUploadService;
import team.proximity.provider_profile_service.validations.AboutValidator;
import team.proximity.provider_profile_service.validations.FileValidator;


import java.io.IOException;
import java.util.Optional;

@Service
public class AboutServiceImpl implements AboutService {

    private final Logger LOGGER = LoggerFactory.getLogger(AboutServiceImpl.class);

    private final AboutBusinessMapper aboutBusinessMapper;
    private final FileUploadService fileStorageService;
    private final AboutRepository aboutRepository;
    private final FileValidator fileValidator;
    private final AboutValidator aboutValidator;

    public AboutServiceImpl(AboutBusinessMapper aboutBusinessMapper, FileUploadService fileStorageService, AboutRepository aboutRepository, FileValidator fileValidator, AboutValidator aboutValidator) {
        this.aboutBusinessMapper = aboutBusinessMapper;
        this.fileStorageService = fileStorageService;
        this.aboutRepository = aboutRepository;
        this.fileValidator = fileValidator;
        this.aboutValidator = aboutValidator;
    }
    public AboutBusinessResponse getAboutForAuthenticatedUser() {
        String authenticatedUsername = AuthHelper.getAuthenticatedUsername();

        if (authenticatedUsername == null) {
            throw new UnauthorizedAccessException("User is not authenticated.");
        }

        About about = aboutRepository.findByCreatedBy(authenticatedUsername)
                .orElseThrow(() -> new AboutNotFoundException("No business found for the authenticated user."));

        return aboutBusinessMapper.mapToResponse(about);
    }


    @Transactional
    public void createOneAbout(AboutRequest aboutRequest) throws IOException {
        fileValidator.validate(aboutRequest.businessIdentityCard());
        fileValidator.validate(aboutRequest.businessCertificate());

        LOGGER.info("Processing About record for user: {}", AuthHelper.getAuthenticatedUsername());

        Optional<About> existingAbout = aboutRepository.findByCreatedBy(aboutRequest.userEmail());
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
                .createdBy(aboutRequest.userEmail())
                .build();

        aboutRepository.save(about);

        LOGGER.info("Successfully created About record for user: {}", AuthHelper.getAuthenticatedUsername());
    }


}


