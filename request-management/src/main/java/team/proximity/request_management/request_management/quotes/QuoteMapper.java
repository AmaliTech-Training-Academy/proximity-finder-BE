package team.proximity.request_management.request_management.quotes;

import org.springframework.stereotype.Component;
import team.proximity.request_management.request_management.descision.DecisionResponse;
import team.proximity.request_management.request_management.descision.QuoteDecision;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class QuoteMapper {

    public Quote mapToQuote(QuoteRequest quoteRequest) {
        return Quote.builder()
                .title(quoteRequest.title())
                .description(quoteRequest.description())
                .location(quoteRequest.location())
                .startDate(quoteRequest.startDate())
                .endDate(quoteRequest.endDate())
                .startTime(quoteRequest.startTime())
                .endTime(quoteRequest.endTime())
                .additionalDetails(quoteRequest.additionalDetails())
                .status(QuoteStatus.UNAPPROVED)
                .assignedProvider(quoteRequest.assignedProvider())
                .build();
    }

    public QuoteResponse mapToQuoteResponse(Quote quote) {
        return new QuoteResponse(
                quote.getQuoteId(),
                quote.getTitle(),
                quote.getDescription(),
                quote.getLocation(),
                quote.getAdditionalDetails(),
                quote.getStatus().toString(),
                quote.getStartDate(),
                quote.getStartTime(),
                quote.getEndDate(),
                quote.getEndTime(),
                quote.getCreatedBy(),
                quote.getAssignedProvider(),
                mapImages(quote),
                mapDecision(quote.getDecision()),
                calculateDuration(quote.getStartDate(), quote.getEndDate()
        ));
    }

    private List<String> mapImages(Quote quote) {
        return quote.getImages()
                .stream()
                .map(QuoteImage::getFilePath)
                .toList();
    }

    private DecisionResponse mapDecision(QuoteDecision decision) {
        return decision == null ? null :
                new DecisionResponse(
                        decision.getPrice(),
                        decision.getApprovalDetails(),
                        decision.getDeclineReason()
                );
    }

    private String calculateDuration(String startDate, String endDate) {
        DateTimeFormatter formatter = new DateTimeFormatterBuilder()
                .appendPattern("[d/M/yyyy]")
                .appendPattern("[dd/MM/yyyy]")
                .toFormatter();

        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);

        long weeks = ChronoUnit.WEEKS.between(start, end);
        return weeks > 0 ? weeks + " weeks" : "Less than a week";
    }
}
