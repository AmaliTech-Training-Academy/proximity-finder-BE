package team.proximity.request_management.request_management.quotes;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public record QuoteRequest(
        @NotBlank(message = "Title is required")
        @Size(max = 255, message = "Title must not exceed 255 characters")
        String title,

        @NotBlank(message = "Description is required")
        @Size(max = 500, message = "Description must not exceed 500 characters")
        String description,

        @Size(max = 1000, message = "Additional details must not exceed 1000 characters")
        String additionalDetails,

        @NotBlank(message = "Location is required")
        @Size(max = 255, message = "Location must not exceed 255 characters")
        String location,

        @NotBlank(message = "Start date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        String startDate,

        @NotBlank(message = "Start time is required")
        @Pattern(
                regexp = "\\d{2}:\\d{2}",
                message = "Start time must be in the format HH:mm, e.g., 00:00"
        )
        String startTime,

        @NotBlank(message = "End date is required")
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
        String endDate,

        @NotBlank(message = "End time is required")
        @Pattern(
                regexp = "\\d{2}:\\d{2}",
                message = "End time must be in the format HH:mm, e.g., 00:00"
        )
        String endTime,

        @NotBlank(message = "Assigned provider is required")
        String assignedProvider,

        @Size(max = 10000, message = "You can upload a maximum of 10000 images")
        List<MultipartFile> images
) {}
