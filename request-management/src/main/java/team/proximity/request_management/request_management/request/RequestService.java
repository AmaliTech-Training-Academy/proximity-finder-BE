package team.proximity.request_management.request_management.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import team.proximity.request_management.request_management.quotes.QuoteResponse;

import java.util.List;

public interface RequestService {

    Page<RequestResponse> findAssignedRequests(Pageable pageable);
    QuoteResponse getQuoteResponseByRequestId(Long requestId);
}
