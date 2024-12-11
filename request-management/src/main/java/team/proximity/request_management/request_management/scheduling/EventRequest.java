package team.proximity.request_management.request_management.scheduling;

public record EventRequest(
    String title,
    String startDate,
    String startTime,
    String endDate,
    String endTime,
    String description
) {}