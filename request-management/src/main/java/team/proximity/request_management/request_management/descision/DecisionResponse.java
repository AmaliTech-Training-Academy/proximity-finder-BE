package team.proximity.request_management.request_management.descision;

import java.math.BigDecimal;

public record DecisionResponse(
        BigDecimal price,
        String approvalDetails,
        String declineReason
) {}
