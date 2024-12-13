package team.proximity.request_management.request_management.scheduling;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record EventRequest(
    @NotBlank(message = "Title is required")
    String title,

    @NotBlank(message = "Start date is required")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "Start date must be in format dd/mm/yyyy")
    String startDate,

    @NotBlank(message = "Start time is required")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "Start time must be in format HH:mm")
    String startTime,

    @NotBlank(message = "End date is required")
    @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[0-2])/\\d{4}$", message = "End date must be in format dd/mm/yyyy")
    String endDate,

    @NotBlank(message = "End time is required")
    @Pattern(regexp = "^([01]\\d|2[0-3]):([0-5]\\d)$", message = "End time must be in format HH:mm")
    String endTime,

    @NotBlank(message = "Description is required")
    String description
) {}