package team.proximity.request_management.request_management.call_request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CallRequestServiceImplTest {

    @Mock
    private CallRequestRepository callRequestRepository;

    @Mock
    private CallRequestMapper callRequestMapper;

    @Captor
    private ArgumentCaptor<CallRequest> callRequestCaptor;

    private CallRequestServiceImpl callRequestService;

    private static final String TEST_PHONE_NUMBER = "+1234567890";
    private static final String TEST_PROVIDER = "testProvider";
    private static final String TEST_EMAIL = "test@example.com";
    private static final Long TEST_REQUEST_ID = 1L;

    @BeforeEach
    void setUp() {
        callRequestService = new CallRequestServiceImpl(callRequestRepository, callRequestMapper);
    }

    @Test
    void createCallRequest_WhenNoDuplicates_ShouldSaveCallRequest() {

        SeekerCallRequest seekerCallRequest = new SeekerCallRequest(TEST_PHONE_NUMBER, TEST_PROVIDER);
        CallRequest mappedCallRequest = new CallRequest();
        when(callRequestRepository.findByPhoneNumberAndAssignedProvider(TEST_PHONE_NUMBER, TEST_PROVIDER))
                .thenReturn(Optional.empty());
        when(callRequestMapper.mapToCallRequest(seekerCallRequest)).thenReturn(mappedCallRequest);


        callRequestService.createCallRequest(seekerCallRequest);


        verify(callRequestRepository).save(mappedCallRequest);
    }

    @Test
    void createCallRequest_WhenDuplicateExists_ShouldThrowException() {

        SeekerCallRequest seekerCallRequest = new SeekerCallRequest(TEST_PHONE_NUMBER, TEST_PROVIDER);
        when(callRequestRepository.findByPhoneNumberAndAssignedProvider(TEST_PHONE_NUMBER, TEST_PROVIDER))
                .thenReturn(Optional.of(new CallRequest()));


        assertThatThrownBy(() -> callRequestService.createCallRequest(seekerCallRequest))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("A call request for this phone number and provider already exists.");

        verify(callRequestMapper, never()).mapToCallRequest(any());
        verify(callRequestRepository, never()).save(any());
    }

    @Test
    void getAllCallRequests_ShouldReturnPagedResults() {

        Pageable pageable = PageRequest.of(0, 10);
        CallRequest callRequest = new CallRequest();
        Page<CallRequest> callRequestPage = new PageImpl<>(List.of(callRequest));
        ProviderCallRequestResponse expectedResponse = new ProviderCallRequestResponse(
                1L, "name", "phone", "email", "PENDING", "provider", "date");

        try (MockedStatic<SecurityContextUtils> mockedStatic = mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getEmail).thenReturn(TEST_EMAIL);
            when(callRequestRepository.findByAssignedProvider(TEST_EMAIL, pageable))
                    .thenReturn(callRequestPage);
            when(callRequestMapper.mapToProviderCallRequestResponse(callRequest))
                    .thenReturn(expectedResponse);


            Page<ProviderCallRequestResponse> result = callRequestService.getAllCallRequests(pageable);


            assertThat(result).isNotNull();
            assertThat(result.getContent()).hasSize(1);
            assertThat(result.getContent().get(0)).isEqualTo(expectedResponse);
        }
    }

    @Test
    void getCallRequestById_WhenExists_ShouldReturnCallRequest() {

        CallRequest callRequest = new CallRequest();
        ProviderCallRequestResponse expectedResponse = new ProviderCallRequestResponse(
                1L, "name", "phone", "email", "PENDING", "provider", "date");

        try (MockedStatic<SecurityContextUtils> mockedStatic = mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getEmail).thenReturn(TEST_EMAIL);
            when(callRequestRepository.findByRequestIdAndAssignedProvider(TEST_REQUEST_ID, TEST_EMAIL))
                    .thenReturn(Optional.of(callRequest));
            when(callRequestMapper.mapToProviderCallRequestResponse(callRequest))
                    .thenReturn(expectedResponse);


            ProviderCallRequestResponse result = callRequestService.getCallRequestById(TEST_REQUEST_ID);


            assertThat(result).isEqualTo(expectedResponse);
        }
    }

    @Test
    void getCallRequestById_WhenNotExists_ShouldThrowException() {

        try (MockedStatic<SecurityContextUtils> mockedStatic = mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getEmail).thenReturn(TEST_EMAIL);
            when(callRequestRepository.findByRequestIdAndAssignedProvider(TEST_REQUEST_ID, TEST_EMAIL))
                    .thenReturn(Optional.empty());


            assertThatThrownBy(() -> callRequestService.getCallRequestById(TEST_REQUEST_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("CallRequest not found or not assigned to the current provider.");
        }
    }

    @Test
    void completeCallRequest_WhenExists_ShouldUpdateStatus() {

        CallRequest callRequest = new CallRequest();
        try (MockedStatic<SecurityContextUtils> mockedStatic = mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getEmail).thenReturn(TEST_EMAIL);
            when(callRequestRepository.findByRequestIdAndAssignedProvider(TEST_REQUEST_ID, TEST_EMAIL))
                    .thenReturn(Optional.of(callRequest));


            callRequestService.completeCallRequest(TEST_REQUEST_ID);


            verify(callRequestRepository).save(callRequestCaptor.capture());
            CallRequest savedCallRequest = callRequestCaptor.getValue();
            assertThat(savedCallRequest.getStatus()).isEqualTo(Status.COMPLETED);
        }
    }

    @Test
    void completeCallRequest_WhenNotExists_ShouldThrowException() {

        try (MockedStatic<SecurityContextUtils> mockedStatic = mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getEmail).thenReturn(TEST_EMAIL);
            when(callRequestRepository.findByRequestIdAndAssignedProvider(TEST_REQUEST_ID, TEST_EMAIL))
                    .thenReturn(Optional.empty());


            assertThatThrownBy(() -> callRequestService.completeCallRequest(TEST_REQUEST_ID))
                    .isInstanceOf(IllegalArgumentException.class)
                    .hasMessage("CallRequest not found or not assigned to the current provider.");

            verify(callRequestRepository, never()).save(any());
        }
    }
}
