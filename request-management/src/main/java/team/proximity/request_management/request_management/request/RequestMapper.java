package team.proximity.request_management.request_management.request;

import org.springframework.stereotype.Component;

@Component
public class RequestMapper {
    public RequestResponse mapToResponse(Request request) {
        return new RequestResponse(
                request.getRequestId(),
                request.getClientName(),
                request.getDescription(),
                request.getClientEmail(),
                request.getRequestDate(),
                request.getAssignedProvider()
        );
    }
}
