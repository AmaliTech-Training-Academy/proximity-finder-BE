package team.proximity.request_management.request_management.booking;

public record BookingResponse(
         Long bookingId,
         String startDate,
         String startTime,
         String endDate,
         String endTime,
         String description,
         String createdBy,
         String assignedProvider,
         String status
) {}
