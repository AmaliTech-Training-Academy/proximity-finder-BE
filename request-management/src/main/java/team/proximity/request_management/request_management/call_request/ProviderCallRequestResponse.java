package team.proximity.request_management.request_management.call_request;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

import java.time.LocalDate;

public record ProviderCallRequestResponse(
         Long requestId,
         String clientName,
         String phoneNumber,
         String clientEmail,
         String status,
         String assignedProvider,
         String requestDate
) {
}
