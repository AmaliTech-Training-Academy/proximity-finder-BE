package team.proximity.request_management.request_management.scheduling;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record AvailabilityCheckRequest(

        @NotNull(message = "Scheduling date cannot be null")
        @NotEmpty(message = "Scheduling date cannot be empty")
        @Pattern(regexp = "^(0[1-9]|[12][0-9]|3[01])/(0[1-9]|1[012])/\\d{4}$", message = "Date must be in format dd/MM/yyyy")
        String schedulingDate,

        @NotNull(message = "Estimated hours cannot be null")

        Double estimatedHours,

        @NotNull(message = "Provider email cannot be null")
        @NotEmpty(message = "Provider email cannot be empty")
        String createdBy

) {}
