package team.proximity.request_management.request_management.quotes;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class QuoteServiceImp implements QuoteService {

    private final Logger LOGGER = LoggerFactory.getLogger(QuoteServiceImp.class);

    private final QuoteMapper quoteMapper;
    private final QuoteRepository quoteRepository;
    private final QuoteDecisionRepository quoteDecisionRepository;
    private final FileProcessingService fileProcessingService;
    private final RequestRepository requestRepository;

    public QuoteServiceImp(QuoteMapper quoteMapper, QuoteRepository quoteRepository, QuoteDecisionRepository quoteDecisionRepository, FileProcessingService fileProcessingService, RequestRepository requestRepository) {
        this.quoteMapper = quoteMapper;
        this.quoteRepository = quoteRepository;
        this.quoteDecisionRepository = quoteDecisionRepository;
        this.fileProcessingService = fileProcessingService;
        this.requestRepository = requestRepository;
    }


    public void createQuote(QuoteRequest quoteRequest) {
        Quote quote = quoteMapper.mapToQuote(quoteRequest);

        List<QuoteImage> uploadedImages = quoteRequest.images().stream()
                .map(fileProcessingService::processImage)
                .filter(Objects::nonNull)
                .map(imageUrl -> new QuoteImage(quote, imageUrl))
                .toList();

        quote.setImages(uploadedImages);
        quote.setCreatedBy(SecurityContextUtils.getEmail());
        quoteRepository.save(quote);

        Request request = createRequestFromQuote(quote);

        requestRepository.save(request);

    }

    public void approveQuote(Long quoteId, QuoteDescisionRequest quoteDescisionRequest) {

        Quote quote = quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new SecurityException("You are not authorized to approve this quote."));

        if (!QuoteStatus.UNAPPROVED.equals(quote.getStatus())) {
            throw new IllegalStateException("Only unapproved quotes can be approved.");
        }
        quote.setStatus(QuoteStatus.APPROVED);

        QuoteDecision decision = new QuoteDecision();
        decision.setQuote(quote);
        decision.setPrice(quoteDescisionRequest.price());
        decision.setApprovalDetails(quoteDescisionRequest.approvalDetails());

        quoteDecisionRepository.save(decision);
        quoteRepository.save(quote);
    }

    public void declineQuote(Long quoteId, QuoteDescisionRequest quoteDescisionRequest) {
        Quote quote = quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new SecurityException("You are not authorized to decline this quote."));

        if (!QuoteStatus.UNAPPROVED.equals(quote.getStatus())) {
            throw new IllegalStateException("Only unapproved quotes can be declined.");
        }
        quote.setStatus(QuoteStatus.DECLINED);

        QuoteDecision decision = new QuoteDecision();
        decision.setQuote(quote);
        decision.setDeclineReason(quoteDescisionRequest.declineReason());

        quoteDecisionRepository.save(decision);
        quoteRepository.save(quote);
    }
    public QuoteResponse getQuoteByIdForCreator(Long quoteId) {

        Quote quote = quoteRepository.findByQuoteIdAndCreatedBy(quoteId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found or you are not authorized to view it."));

        return quoteMapper.mapToQuoteResponse(quote);
    }

    public QuoteResponse getQuoteByIdForAssignedProvider(Long quoteId) {

        Quote quote = quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new QuoteNotFoundException("Quote not found or you are not authorized to view it." ));

        return quoteMapper.mapToQuoteResponse(quote);
    }

    public List<QuoteResponse> getQuotesCreatedBy(String createdBy) {
        return quoteRepository.findByCreatedByWithDetails(createdBy)
                .stream()
                .map(quoteMapper::mapToQuoteResponse)
                .toList();
    }

    public List<QuoteResponse> getQuotesAssignedTo(String assignedTo) {
        return quoteRepository.findByAssignedToWithDetails(assignedTo)
                .stream()
                .map(quoteMapper::mapToQuoteResponse)
                .toList();
    }

    private static Request createRequestFromQuote(Quote quote) {
        return Request.builder()
                .clientName(SecurityContextUtils.getUsername())
                .description(quote.getDescription())
                .clientEmail(quote.getCreatedBy())
                .requestDate(quote.getRequestDate().toString())
                .assignedProvider(quote.getAssignedProvider())
                .build();
    }

}
