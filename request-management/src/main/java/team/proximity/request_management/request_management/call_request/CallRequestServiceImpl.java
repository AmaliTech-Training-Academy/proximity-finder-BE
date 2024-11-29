package team.proximity.request_management.request_management.call_request;

import org.springframework.stereotype.Service;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CallRequestServiceImpl implements CallRequestService {

    private final CallRequestRepository callRequestRepository;
    private final CallRequestMapper callRequestMapper;

    public CallRequestServiceImpl(CallRequestRepository callRequestRepository, CallRequestMapper callRequestMapper) {
        this.callRequestRepository = callRequestRepository;
        this.callRequestMapper = callRequestMapper;
    }

    public void createCallRequest(SeekerCallRequest seekerCallRequest) {

        CallRequest callRequest = callRequestMapper.mapToCallRequest(seekerCallRequest);
        callRequestRepository.save(callRequest);
    }

    public List<ProviderCallRequestResponse> getAllCallRequests() {

        return callRequestRepository
                .findByAssignedProvider(SecurityContextUtils.getEmail())
                .stream()
                .map(callRequestMapper::mapToProviderCallRequestResponse)
                .collect(Collectors.toList());
    }

    public ProviderCallRequestResponse getCallRequestById(Long requestId) {

        CallRequest callRequest = callRequestRepository.findByRequestIdAndAssignedProvider(
                requestId, SecurityContextUtils.getEmail()).orElseThrow(() -> new IllegalArgumentException("CallRequest not found or not assigned to the current provider."));

        return callRequestMapper.mapToProviderCallRequestResponse(callRequest);
    }

    public void completeCallRequest(Long requestId) {
        CallRequest callRequest = callRequestRepository.findByRequestIdAndAssignedProvider(
                requestId, SecurityContextUtils.getEmail()).orElseThrow(() -> new IllegalArgumentException("CallRequest not found or not assigned to the current provider."));
        callRequest.setStatus(Status.COMPLETED);
        callRequestRepository.save(callRequest);

    }
}
