package team.proximity.request_management.request_management.quotes;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.descision.QuoteDescisionRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quotes")
public class QuoteController {

    private final QuoteService quoteService;

    public QuoteController(QuoteService quoteService) {
        this.quoteService = quoteService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse> createQuote(@Valid @ModelAttribute QuoteRequest quoteRequest) {
        quoteService.createQuote(quoteRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Quote added successfully"));
    }


    @PutMapping("/{quoteId}/status/approve")
    public ResponseEntity<ApiSuccessResponse> approveQuote(@PathVariable Long quoteId, @Valid @RequestBody QuoteDescisionRequest quoteDecisionRequest) {
        quoteService.approveQuote(quoteId, quoteDecisionRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiSuccessResponse("Quote approved successfully"));
    }


    @PutMapping("/{quoteId}/status/decline")
    public ResponseEntity<ApiSuccessResponse> declineQuote(@PathVariable Long quoteId, @Valid @RequestBody QuoteDescisionRequest quoteDecisionRequest) {
        quoteService.declineQuote(quoteId, quoteDecisionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse("Quote declined successfully"));
    }

    @GetMapping("/creator")
    public List<QuoteResponse> getQuotesByCreator(@RequestParam String createdBy) {
        return quoteService.getQuotesCreatedBy(createdBy);
    }


    @GetMapping("/provider")
    public List<QuoteResponse> getQuotesByAssignee(@RequestParam String assignedTo) {
        return quoteService.getQuotesAssignedTo(assignedTo);
    }

    @GetMapping("/{quoteId}/creator/details")
    public ResponseEntity<QuoteResponse> getQuoteDetailsForCreator(@PathVariable Long quoteId) {
        QuoteResponse quoteResponse = quoteService.getQuoteByIdForCreator(quoteId);
        return ResponseEntity.ok(quoteResponse);
    }

    @GetMapping("/{quoteId}/provider/details")
    public ResponseEntity<QuoteResponse> getQuoteDetailsForAssignee(@PathVariable Long quoteId) {
        QuoteResponse quoteResponse = quoteService.getQuoteByIdForAssignedProvider(quoteId);
        return ResponseEntity.ok(quoteResponse);
    }
}
