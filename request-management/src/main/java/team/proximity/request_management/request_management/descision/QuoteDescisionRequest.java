package team.proximity.request_management.request_management.descision;

import java.math.BigDecimal;

public record QuoteDescisionRequest(

         Long id,
         BigDecimal price,
         String approvalDetails,
         String declineReason
) {}
