package team.proximity.service_provider_profile.about_your_business.about;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;


public record AboutRequest(
        LocalDate inceptionDate,
        Set<String> socialMediaLinks,
        Integer numberOfEmployees,
        MultipartFile businessIdentityCardFile,
        MultipartFile businessCertificateFile,
        String businessSummary

) {}
