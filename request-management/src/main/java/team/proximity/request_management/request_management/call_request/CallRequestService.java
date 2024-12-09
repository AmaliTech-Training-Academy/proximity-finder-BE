package team.proximity.request_management.request_management.call_request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CallRequestService {
    void createCallRequest(SeekerCallRequest seekerCallRequest);
    Page<ProviderCallRequestResponse> getAllCallRequests(Pageable pageable);
    ProviderCallRequestResponse getCallRequestById(Long requestId);
    void completeCallRequest(Long requestId);
}
