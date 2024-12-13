package team.proximity.request_management.request_management.booking;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record BookingRequest(
        @NotNull(message = "Start date is required")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "Start date must be in format DD/MM/YYYY")
        String startDate,

        @NotNull(message = "Start time is required")
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "Start time must be in format HH:mm")
        String startTime,

        @NotNull(message = "End date is required")
        @Pattern(regexp = "^\\d{2}/\\d{2}/\\d{4}$", message = "End date must be in format DD/MM/YYYY")
        String endDate,

        @NotNull(message = "End time is required")
        @Pattern(regexp = "^([01]?[0-9]|2[0-3]):[0-5][0-9]$", message = "End time must be in format HH:mm")
        String endTime,

        @NotBlank(message = "Description is required")
        String description,

        @NotBlank(message = "Provider assignment is required")
        String assignedProvider
) {}