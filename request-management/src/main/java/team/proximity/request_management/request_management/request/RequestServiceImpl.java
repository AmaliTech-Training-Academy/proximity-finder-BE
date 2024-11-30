package team.proximity.request_management.request_management.request;

import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    public List<RequestResponse> findAssignedRequests(String assignedProvider) {

        return requestRepository
                .findByAssignedProvider(assignedProvider)
                .stream()
                .map(requestMapper::mapToResponse)
                .toList();

    }
}
