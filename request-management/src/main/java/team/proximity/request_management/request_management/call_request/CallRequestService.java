package team.proximity.request_management.request_management.call_request;

import java.util.List;

public interface CallRequestService {
    void createCallRequest(SeekerCallRequest seekerCallRequest);
    List<ProviderCallRequestResponse> getAllCallRequests();
    ProviderCallRequestResponse getCallRequestById(Long requestId);
    void completeCallRequest(Long requestId);
}
