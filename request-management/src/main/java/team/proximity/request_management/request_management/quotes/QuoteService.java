package team.proximity.request_management.request_management.quotes;

import java.util.List;

public interface QuoteService {

    void createQuote(QuoteRequest quoteRequest);
    void approveQuote(Long quoteId, QuoteDescisionRequest quoteDescisionRequest);
    void declineQuote(Long quoteId, QuoteDescisionRequest quoteDescisionRequest);
    List<QuoteResponse> getQuotesAssignedTo(String assignedTo);
    List<QuoteResponse> getQuotesCreatedBy(String createdBy);
    QuoteResponse getQuoteByIdForCreator(Long quoteId);
    QuoteResponse getQuoteByIdForAssignedProvider(Long quoteId);

}
