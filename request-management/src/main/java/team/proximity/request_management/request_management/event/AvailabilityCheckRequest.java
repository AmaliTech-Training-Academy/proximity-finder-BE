package team.proximity.request_management.request_management.event;

public record AvailabilityCheckRequest(
        String schedulingDate,
        String estimatedHours

) {}
