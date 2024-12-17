package team.proximity.request_management.request_management.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.quotes.QuoteResponse;
import team.proximity.request_management.request_management.quotes.QuoteService;

@RestController
@RequestMapping("/api/v1/quote-service/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }


    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @GetMapping("/assigned")
    public ResponseEntity<Page<RequestResponse>> getAssignedRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<RequestResponse> assignedRequests = requestService.findAssignedRequests(pageable);

        return new ResponseEntity<>(assignedRequests, HttpStatus.OK);
    }
    @GetMapping("/quote/{requestId}")
    public ResponseEntity<QuoteResponse> getQuoteByRequestId(@PathVariable Long requestId) {
        QuoteResponse quoteResponse = requestService.getQuoteResponseByRequestId(requestId);
        return ResponseEntity.ok(quoteResponse);
    }
}
