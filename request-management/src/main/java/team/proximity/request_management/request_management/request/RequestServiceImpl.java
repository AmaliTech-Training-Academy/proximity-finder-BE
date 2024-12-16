package team.proximity.request_management.request_management.request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.proximity.request_management.request_management.exception.QuoteNotFoundException;
import team.proximity.request_management.request_management.quotes.Quote;
import team.proximity.request_management.request_management.quotes.QuoteMapper;
import team.proximity.request_management.request_management.quotes.QuoteResponse;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

@Service
public class RequestServiceImpl implements RequestService {

    private final RequestRepository requestRepository;
    private final RequestMapper requestMapper;
    private final QuoteMapper quoteMapper;

    public RequestServiceImpl(RequestRepository requestRepository, RequestMapper requestMapper, QuoteMapper quoteMapper) {
        this.requestRepository = requestRepository;
        this.requestMapper = requestMapper;
        this.quoteMapper = quoteMapper;
    }

    public Page<RequestResponse> findAssignedRequests(Pageable pageable) {
        return requestRepository
                .findByAssignedProvider(SecurityContextUtils.getEmail(), pageable)
                .map(requestMapper::mapToResponse);
    }
    public QuoteResponse getQuoteResponseByRequestId(Long requestId) {
        Quote quote = requestRepository.findById(requestId)
                .map(Request::getQuote)
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found for Request ID: " + requestId));

        return quoteMapper.mapToQuoteResponse(quote);
    }
}
