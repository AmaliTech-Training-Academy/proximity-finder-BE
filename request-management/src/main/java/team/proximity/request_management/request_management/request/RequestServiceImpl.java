package team.proximity.request_management.request_management.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.util.List;
@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;

    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
    }

    public Page<RequestResponse> findAssignedRequests(Pageable pageable) {
        return requestRepository
                .findByAssignedProvider(SecurityContextUtils.getEmail(), pageable)
                .map(requestMapper::mapToResponse);
    }
}
