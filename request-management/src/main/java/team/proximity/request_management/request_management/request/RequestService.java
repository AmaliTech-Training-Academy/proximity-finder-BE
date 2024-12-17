package team.proximity.request_management.request_management.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.proximity.request_management.request_management.quotes.QuoteResponse;

public interface RequestService {

    Page<RequestResponse> findAssignedRequests(Pageable pageable);
    QuoteResponse getQuoteResponseByRequestId(Long requestId);
}
