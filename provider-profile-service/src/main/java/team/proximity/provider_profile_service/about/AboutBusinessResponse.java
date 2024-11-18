package team.proximity.provider_profile_service.about;

import java.time.LocalDate;
import java.util.Set;

public record AboutBusinessResponse(
        Long businessId,
        LocalDate inceptionDate,
        Set<String>socialMediaLinks,
        String businessIdentityCard,
        String businessCertificate,
        Integer numberOfEmployees,
        String businessSummary
) {
}
