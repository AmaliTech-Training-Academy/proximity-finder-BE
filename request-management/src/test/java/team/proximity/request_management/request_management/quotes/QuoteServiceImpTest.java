package team.proximity.request_management.request_management.quotes;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.request_management.request_management.descision.QuoteDecision;
import team.proximity.request_management.request_management.descision.QuoteDecisionRepository;
import team.proximity.request_management.request_management.descision.QuoteDescisionRequest;
import team.proximity.request_management.request_management.exception.DuplicateQuoteException;
import team.proximity.request_management.request_management.exception.QuoteNotFoundException;
import team.proximity.request_management.request_management.fileupload.FileProcessingService;
import team.proximity.request_management.request_management.request.Request;
import team.proximity.request_management.request_management.request.RequestRepository;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class QuoteServiceImpTest {

    @Mock
    private QuoteMapper quoteMapper;
    @Mock
    private QuoteRepository quoteRepository;
    @Mock
    private QuoteDecisionRepository quoteDecisionRepository;
    @Mock
    private FileProcessingService fileProcessingService;
    @Mock
    private SecurityContextUtils securityContextUtils;
    @Mock
    private RequestRepository requestRepository;
    @InjectMocks
    private QuoteServiceImp quoteService;

    @Test
    void createQuote_SuccessfulCreation() {

        QuoteRequest quoteRequest = new QuoteRequest(
                "Test Quote",
                "Test Description",
                "Additional Details",
                "Test Location",
                "January 01, 2024",
                "09:00",
                "January 02, 2024",
                "17:00",
                "provider1",
                List.of()
        );

        Quote mockQuote = new Quote();
        when(quoteMapper.mapToQuote(any())).thenReturn(mockQuote);
        when(quoteRepository.existsByTitleAndAssignedProvider(anyString(), anyString()))
                .thenReturn(false);


        quoteService.createQuote(quoteRequest);


        verify(quoteRepository).save(any(Quote.class));
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void createQuote_ThrowsDuplicateException() {

        QuoteRequest quoteRequest = new QuoteRequest(
                "Existing Quote",
                "Test Description",
                "Additional Details",
                "Test Location",
                "January 01, 2024",
                "09:00",
                "January 02, 2024",
                "17:00",
                "provider1",
                List.of()
        );

        when(quoteRepository.existsByTitleAndAssignedProvider(
                quoteRequest.title(),
                quoteRequest.assignedProvider()
        )).thenReturn(true);


        assertThrows(DuplicateQuoteException.class, () ->
                quoteService.createQuote(quoteRequest)
        );

        verify(quoteRepository, never()).save(any());
        verify(requestRepository, never()).save(any());
    }

    @Test
    void createQuote_WithImages_ProcessesImagesSuccessfully() {

        MultipartFile mockFile = mock(MultipartFile.class);
        QuoteRequest quoteRequest = new QuoteRequest(
                "Test Quote",
                "Test Description",
                "Additional Details",
                "Test Location",
                "January 01, 2024",
                "09:00",
                "January 02, 2024",
                "17:00",
                "provider1",
                List.of(mockFile)
        );

        Quote mockQuote = new Quote();
        when(quoteMapper.mapToQuote(any())).thenReturn(mockQuote);
        when(quoteRepository.existsByTitleAndAssignedProvider(anyString(), anyString()))
                .thenReturn(false);
        when(fileProcessingService.processImage(any())).thenReturn("processed-image-url");


        quoteService.createQuote(quoteRequest);


        verify(fileProcessingService).processImage(any());
        verify(quoteRepository).save(any(Quote.class));
        verify(requestRepository).save(any(Request.class));
    }

    @Test
    void approveQuote_Success() {
        Long quoteId = 1L;
        String providerEmail = "provider@example.com";

        Quote quote = new Quote();
        quote.setQuoteId(quoteId);
        quote.setStatus(QuoteStatus.UNAPPROVED);

        QuoteDescisionRequest request = new QuoteDescisionRequest(
                quoteId,
                new BigDecimal("100.00"),
                "Approved with standard terms",
                null
        );


        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(providerEmail);

            when(quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, providerEmail))
                    .thenReturn(Optional.of(quote));


            quoteService.approveQuote(quoteId, request);


            assertEquals(QuoteStatus.APPROVED, quote.getStatus());
            verify(quoteDecisionRepository, times(1)).save(any(QuoteDecision.class));
            verify(quoteRepository, times(1)).save(quote);
        }
    }

    @Test
    void approveQuote_UnauthorizedAccess_ThrowsSecurityException() {

        Long quoteId = 1L;
        String providerEmail = "provider@example.com";

        QuoteDescisionRequest request = new QuoteDescisionRequest(
                quoteId,
                new BigDecimal("100.00"),
                "Approved with standard terms",
                null
        );

        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(providerEmail);

            when(quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, providerEmail))
                    .thenReturn(Optional.empty());


            assertThrows(SecurityException.class, () ->
                    quoteService.approveQuote(quoteId, request)
            );

            verify(quoteDecisionRepository, never()).save(any());
            verify(quoteRepository, never()).save(any());
        }
    }


    @Test
    void declineQuote_Success() {

        Long quoteId = 1L;
        String providerEmail = "provider@example.com";

        Quote quote = new Quote();
        quote.setQuoteId(quoteId);
        quote.setStatus(QuoteStatus.UNAPPROVED);

        QuoteDescisionRequest request = new QuoteDescisionRequest(
                quoteId,
                null,
                null,
                "Price too high for the scope"
        );

        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(providerEmail);

            when(quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, providerEmail))
                    .thenReturn(Optional.of(quote));


            quoteService.declineQuote(quoteId, request);

            assertEquals(QuoteStatus.DECLINED, quote.getStatus());
            verify(quoteDecisionRepository, times(1)).save(any(QuoteDecision.class));
            verify(quoteRepository, times(1)).save(quote);
        }
    }

    @Test
    void declineQuote_UnauthorizedAccess_ThrowsSecurityException() {

        Long quoteId = 1L;
        String providerEmail = "provider@example.com";

        QuoteDescisionRequest request = new QuoteDescisionRequest(
                quoteId,
                null,
                null,
                "Price too high for the scope"
        );

        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(providerEmail);

            when(quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, providerEmail))
                    .thenReturn(Optional.empty());

            assertThrows(SecurityException.class, () ->
                    quoteService.declineQuote(quoteId, request)
            );

            verify(quoteDecisionRepository, never()).save(any());
            verify(quoteRepository, never()).save(any());
        }
    }




    @Test
    void getQuoteByIdForCreator_Success() {
        Long quoteId = 1L;
        String userEmail = "creator@example.com";
        Quote quote = new Quote();
        QuoteResponse expectedResponse = createSampleQuoteResponse();

        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(userEmail);

            when(quoteRepository.findByQuoteIdAndCreatedBy(quoteId, userEmail))
                    .thenReturn(Optional.of(quote));
            when(quoteMapper.mapToQuoteResponse(quote)).thenReturn(expectedResponse);

            QuoteResponse result = quoteService.getQuoteByIdForCreator(quoteId);

            assertNotNull(result);
            assertEquals(expectedResponse.quoteId(), result.quoteId());
            assertEquals(expectedResponse.title(), result.title());
            assertEquals(expectedResponse.createdBy(), result.createdBy());
            verify(quoteRepository).findByQuoteIdAndCreatedBy(quoteId, userEmail);
            verify(quoteMapper).mapToQuoteResponse(quote);
        }
    }

    @Test
    void getQuoteByIdForCreator_NotFound_ThrowsException() {

        Long quoteId = 1L;
        String userEmail = "creator@example.com";

        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(userEmail);

            when(quoteRepository.findByQuoteIdAndCreatedBy(quoteId, userEmail))
                    .thenReturn(Optional.empty());


            assertThrows(QuoteNotFoundException.class, () ->
                    quoteService.getQuoteByIdForCreator(quoteId)
            );
        }
    }

    @Test
    void getQuoteByIdForAssignedProvider_Success() {

        Long quoteId = 1L;
        String providerEmail = "provider@example.com";
        Quote quote = new Quote();
        QuoteResponse expectedResponse = createSampleQuoteResponse();

        try (MockedStatic<SecurityContextUtils> utilities = mockStatic(SecurityContextUtils.class)) {
            utilities.when(SecurityContextUtils::getEmail).thenReturn(providerEmail);

            when(quoteRepository.findByQuoteIdAndAssignedProvider(quoteId, providerEmail))
                    .thenReturn(Optional.of(quote));
            when(quoteMapper.mapToQuoteResponse(quote)).thenReturn(expectedResponse);

            QuoteResponse actualResponse = quoteService.getQuoteByIdForAssignedProvider(quoteId);

            assertNotNull(actualResponse, "Response should not be null");
            assertEquals(expectedResponse, actualResponse, "Response should match expected response");

            verify(quoteRepository, times(1))
                    .findByQuoteIdAndAssignedProvider(quoteId, providerEmail);
            verify(quoteMapper, times(1)).mapToQuoteResponse(quote);

            utilities.verify(SecurityContextUtils::getEmail, times(1));
        }
    }


    private QuoteResponse createSampleQuoteResponse() {
        return new QuoteResponse(
                1L,
                "Sample Quote",
                "Description",
                "New York",
                "Additional details",
                "PENDING",
                "2024-01-20",
                "10:00",
                "2024-01-21",
                "17:00",
                "creator@example.com",
                "provider@example.com",
                List.of("image1.jpg"),
                null,
                "8 Weeks"
        );
    }
}