package team.proximity.request_management.request_management.quotes;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.descision.QuoteDescisionRequest;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quote-service/quotes")
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


    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @PutMapping("/{quoteId}/status/approve")
    public ResponseEntity<ApiSuccessResponse> approveQuote(@PathVariable Long quoteId, @Valid @RequestBody QuoteDescisionRequest quoteDecisionRequest) {
        quoteService.approveQuote(quoteId, quoteDecisionRequest);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new ApiSuccessResponse("Quote approved successfully"));
    }

    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @PutMapping("/{quoteId}/status/decline")
    public ResponseEntity<ApiSuccessResponse> declineQuote(@PathVariable Long quoteId, @Valid @RequestBody QuoteDescisionRequest quoteDecisionRequest) {
        quoteService.declineQuote(quoteId, quoteDecisionRequest);
        return ResponseEntity.status(HttpStatus.OK).body(new ApiSuccessResponse("Quote declined successfully"));
    }

    @PreAuthorize("hasAuthority('ROLE_SEEKER')")
    @GetMapping("/creator")
    public List<QuoteResponse> getQuotesByCreator() {
        return quoteService.getQuotesCreatedBy();
    }

    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @GetMapping("/provider")
    public List<QuoteResponse> getQuotesByAssignee() {
        return quoteService.getQuotesAssignedTo();
    }

    @PreAuthorize("hasAuthority('ROLE_SEEKER')")
    @GetMapping("/{quoteId}/creator/details")
    public ResponseEntity<QuoteResponse> getQuoteDetailsForCreator(@PathVariable Long quoteId) {
        QuoteResponse quoteResponse = quoteService.getQuoteByIdForCreator(quoteId);
        return ResponseEntity.ok(quoteResponse);
    }
    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @GetMapping("/{quoteId}/provider/details")
    public ResponseEntity<QuoteResponse> getQuoteDetailsForAssignee(@PathVariable Long quoteId) {
        QuoteResponse quoteResponse = quoteService.getQuoteByIdForAssignedProvider(quoteId);
        return ResponseEntity.ok(quoteResponse);
    }
}
