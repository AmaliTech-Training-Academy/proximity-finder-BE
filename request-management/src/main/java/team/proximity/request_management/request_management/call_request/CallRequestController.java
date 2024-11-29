package team.proximity.request_management.request_management.call_request;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.quotes.ApiSuccessResponse;

import java.util.List;

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


    @GetMapping
    public ResponseEntity<List<ProviderCallRequestResponse>> getAllCallRequests() {
        List<ProviderCallRequestResponse> callRequests = callRequestService.getAllCallRequests();
        return ResponseEntity.ok(callRequests);
    }


    @GetMapping("/{requestId}")
    public ResponseEntity<ProviderCallRequestResponse> getCallRequestById(@PathVariable Long requestId) {
        ProviderCallRequestResponse callRequest = callRequestService.getCallRequestById(requestId);
        return ResponseEntity.ok(callRequest);
    }


    @PatchMapping("/{requestId}/complete")
    public ResponseEntity<ApiSuccessResponse> completeCallRequest(@PathVariable Long requestId) {
        callRequestService.completeCallRequest(requestId);
        return ResponseEntity.ok(new ApiSuccessResponse("Call request marked as completed."));
    }
}
