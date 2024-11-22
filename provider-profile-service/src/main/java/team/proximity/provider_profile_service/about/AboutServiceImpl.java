package team.proximity.provider_profile_service.about;

import jakarta.transaction.Transactional;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.apache.tomcat.util.http.fileupload.impl.FileUploadIOException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
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
public void createOneAbout(AboutRequest aboutRequest) {

        fileValidator.validate(aboutRequest.businessIdentityCard());
        fileValidator.validate(aboutRequest.businessCertificate());

        LOGGER.info("Processing About record for user: {}", AuthHelper.getAuthenticatedUsername());

        Optional<About> existingAbout = aboutRepository.findByCreatedBy(AuthHelper.getAuthenticatedUsername());
        existingAbout.ifPresent(about -> {
            LOGGER.info("Deleting existing About record for user: {}", AuthHelper.getAuthenticatedUsername());
            aboutRepository.delete(about);
        });

        String businessIdentityCardPath = uploadFileSafely(aboutRequest.businessIdentityCard());
        String businessCertificatePath = uploadFileSafely(aboutRequest.businessCertificate());

        About about = createAboutFromRequest(aboutRequest, businessIdentityCardPath, businessCertificatePath);

        aboutRepository.save(about);
        LOGGER.info("Successfully created About record for user: {}", AuthHelper.getAuthenticatedUsername());

}
    private String uploadFileSafely(MultipartFile file) {
        try {
            String filePath = fileStorageService.uploadFile(file);
            LOGGER.info("File uploaded successfully for user: {}", AuthHelper.getAuthenticatedUsername());
            return filePath;
        } catch (IOException e) {
            LOGGER.error("File upload failed for user: {}", AuthHelper.getAuthenticatedUsername(), e);
            return null;
        }
    }

    private static About createAboutFromRequest(AboutRequest aboutRequest, String businessIdentityCardPath, String businessCertificatePath) {
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
}


