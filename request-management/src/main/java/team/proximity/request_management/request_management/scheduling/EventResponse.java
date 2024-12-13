package team.proximity.request_management.request_management.scheduling;

public record EventResponse(
        Long eventId,
        String title,
        String startDate,
        String startTime,
        String endDate,
        String endTime,
        String description,
        String createdBy
) {}
