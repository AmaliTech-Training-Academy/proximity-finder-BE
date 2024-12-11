package team.proximity.request_management.request_management.call_request;

import jakarta.persistence.EntityExistsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

@Service
public class CallRequestServiceImpl implements CallRequestService {

    private final CallRequestRepository callRequestRepository;
    private final CallRequestMapper callRequestMapper;

    public CallRequestServiceImpl(CallRequestRepository callRequestRepository, CallRequestMapper callRequestMapper) {
        this.callRequestRepository = callRequestRepository;
        this.callRequestMapper = callRequestMapper;
    }

    public void createCallRequest(SeekerCallRequest seekerCallRequest) {
        checkForDuplicateCallRequest(seekerCallRequest);

        CallRequest callRequest = callRequestMapper.mapToCallRequest(seekerCallRequest);
        callRequestRepository.save(callRequest);
    }


    public Page<ProviderCallRequestResponse> getAllCallRequests(Pageable pageable) {

        return callRequestRepository
                .findByAssignedProvider(SecurityContextUtils.getEmail(), pageable)
                .map(callRequestMapper::mapToProviderCallRequestResponse);
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

    private void checkForDuplicateCallRequest(SeekerCallRequest seekerCallRequest) {
        boolean exists = callRequestRepository
                .findByPhoneNumberAndAssignedProvider(seekerCallRequest.phoneNumber(), seekerCallRequest.assignedProvider())
                .isPresent();

        if (exists) {
            throw new EntityExistsException("A call request for this phone number and provider already exists.");
        }
    }
}
