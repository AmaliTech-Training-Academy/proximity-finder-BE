package team.proximity.service_provider_profile.about_your_business;

import org.springframework.validation.annotation.Validated;

import java.time.LocalDate;
import java.util.Set;


public record AboutRequest(
        LocalDate inceptionDate,
        Set<String> socialMediaLinks,
        Integer numberOfEmployees,
        String businessSummary

) {}