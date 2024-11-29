package team.proximity.request_management.request_management.request;

import java.util.List;

public interface RequestService {

    List<RequestResponse> findAssignedRequests();
}
