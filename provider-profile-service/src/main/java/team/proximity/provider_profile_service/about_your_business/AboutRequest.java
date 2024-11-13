package team.proximity.provider_profile_service.about_your_business;

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
