package team.proximity.request_management.request_management.call_request;

import org.aspectj.weaver.ast.Call;
import org.springframework.stereotype.Component;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

@Component
public class CallRequestMapper {

    public CallRequest mapToCallRequest(SeekerCallRequest seekerCallRequest) {

        return CallRequest.builder()
                .clientName(SecurityContextUtils.getUsername())
                .phoneNumber(seekerCallRequest.phoneNumber())
                .clientEmail(SecurityContextUtils.getEmail())
                .status(Status.PENDING)
                .assignedProvider(seekerCallRequest.assignedProvider())
                .build();

    }

    public ProviderCallRequestResponse mapToProviderCallRequestResponse(CallRequest callRequest) {
        return new ProviderCallRequestResponse(
                callRequest.getRequestId(),
                callRequest.getClientName(),
                callRequest.getPhoneNumber(),
                callRequest.getClientEmail(),
                callRequest.getStatus().toString(),
                callRequest.getAssignedProvider(),
                callRequest.getRequestDate().toString()
        );
    }


}
