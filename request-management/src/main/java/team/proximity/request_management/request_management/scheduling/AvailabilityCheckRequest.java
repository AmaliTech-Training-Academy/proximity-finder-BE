package team.proximity.request_management.request_management.scheduling;

public record AvailabilityCheckRequest(

        String schedulingDate,
        String estimatedHours,
        String createdBy

) {}
