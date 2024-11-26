package team.proximity.provider_profile_service.about;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.Set;

@Schema(description = "Request to create an About entity")
public record AboutRequest(

        @NotNull(message = "Inception date is required")
        @PastOrPresent(message = "Inception date must be in the past or present")
        LocalDate inceptionDate,


        @Size(max = 5, message = "Maximum 5 social media links allowed")
        Set<String> socialMediaLinks,

        @NotNull(message = "Number of employees is required")
        @Min(value = 1, message = "Number of employees must be at least 1")
        @Max(value = 1000000, message = "Number of employees cannot exceed 1 million")
        Integer numberOfEmployees,

        @NotNull(message = "Business identity card is required")
        MultipartFile businessIdentityCard,


        @NotNull(message = "Business certificate is required")
        MultipartFile businessCertificate,

        @NotNull(message = "Business summary is required")
        @NotBlank(message = "Business summary cannot be blank")
        @Size(min = 10, max = 1000, message = "Business summary must be between 10 and 1000 characters")
        @Pattern(regexp = "^[\\p{L}\\p{N}\\s.,!?()-]+$", message = "Business summary contains invalid characters")
        String businessSummary
) {}
