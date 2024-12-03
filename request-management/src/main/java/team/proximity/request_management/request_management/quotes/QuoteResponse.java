package team.proximity.request_management.request_management.quotes;

import com.fasterxml.jackson.annotation.JsonInclude;
import team.proximity.request_management.request_management.descision.DecisionResponse;

import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record QuoteResponse(
        Long quoteId,
        String title,
        String description,
        String location,
        String additionalDetails,
        String status,
        String startDate,
        String startTime,
        String endDate,
        String endTime,
        String createdBy,
        String assignedProvider,
        List<String> images,
        DecisionResponse decision,
        String duration
) {}
