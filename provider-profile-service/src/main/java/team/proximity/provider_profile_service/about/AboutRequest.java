package team.proximity.provider_profile_service.about;

import jakarta.validation.constraints.*;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;


@Validated
public record AboutRequest(
        @NotNull(message = "Inception date is required")
        @PastOrPresent(message = "Inception date must be in the past or present")
        LocalDate inceptionDate,

        Set<String> socialMediaLinks,

        @NotNull(message = "Number of employees is required")
        @Min(value = 1, message = "Number of employees must be at least 1")
        Integer numberOfEmployees,

        MultipartFile businessIdentityCard,
        MultipartFile businessCertificate,

        @NotNull(message = "Business summary is required")
        @NotBlank(message = "Business summary cannot be blank")
        @Size(min = 10, max = 1000, message = "Business summary must be between 10 and 1000 characters")
        String businessSummary

) {}
