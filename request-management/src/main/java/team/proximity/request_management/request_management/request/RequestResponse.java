package team.proximity.request_management.request_management.request;

public record RequestResponse(
         Long requestId,
         String clientName,
         String description,
         String clientEmail,
         String requestDate,
         String assignedProvider

) {}
