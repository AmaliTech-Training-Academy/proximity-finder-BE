package team.proximity.request_management.request_management.call_request;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.quotes.ApiSuccessResponse;

@RestController
@RequestMapping("/api/v1/quote-service/call-request")
public class CallRequestController {

    private final CallRequestService callRequestService;

    public CallRequestController(CallRequestServiceImpl callRequestService) {
        this.callRequestService = callRequestService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse> createCallRequest(@Valid @RequestBody SeekerCallRequest seekerCallRequest) {
        callRequestService.createCallRequest(seekerCallRequest);
        return ResponseEntity.ok(new ApiSuccessResponse("Call request created successfully."));
    }

    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @GetMapping
    public ResponseEntity<Page<ProviderCallRequestResponse>> getAllCallRequests(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "requestDate") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection
    ) {
        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        PageRequest pageable = PageRequest.of(page, size, sort);
        Page<ProviderCallRequestResponse> callRequests = callRequestService.getAllCallRequests(pageable);

        return new ResponseEntity<>(callRequests, HttpStatus.OK);

    }

    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @GetMapping("/{requestId}")
    public ResponseEntity<ProviderCallRequestResponse> getCallRequestById(@PathVariable Long requestId) {
        ProviderCallRequestResponse callRequest = callRequestService.getCallRequestById(requestId);
        return ResponseEntity.ok(callRequest);
    }

    @PreAuthorize("hasAuthority('ROLE_PROVIDER')")
    @PatchMapping("/{requestId}/complete")
    public ResponseEntity<ApiSuccessResponse> completeCallRequest(@PathVariable Long requestId) {
        callRequestService.completeCallRequest(requestId);
        return ResponseEntity.ok(new ApiSuccessResponse("Call request marked as completed."));
    }
}
