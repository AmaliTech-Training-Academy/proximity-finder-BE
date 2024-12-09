package team.proximity.request_management.request_management.call_request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
class CallRequestMapperTest {

    private CallRequestMapper callRequestMapper;

    @BeforeEach
    void setUp() {
        callRequestMapper = new CallRequestMapper();
    }

    @Test
    void mapToCallRequest_ShouldMapAllFieldsCorrectly() {

        String expectedUsername = "testUser";
        String expectedEmail = "test@example.com";
        String phoneNumber = "+1234567890";
        String assignedProvider = "Provider1";

        SeekerCallRequest seekerCallRequest = new SeekerCallRequest(phoneNumber, assignedProvider);

        try (MockedStatic<SecurityContextUtils> mockedStatic = Mockito.mockStatic(SecurityContextUtils.class)) {
            mockedStatic.when(SecurityContextUtils::getUsername).thenReturn(expectedUsername);
            mockedStatic.when(SecurityContextUtils::getEmail).thenReturn(expectedEmail);


            CallRequest result = callRequestMapper.mapToCallRequest(seekerCallRequest);


            assertThat(result).isNotNull();
            assertThat(result.getClientName()).isEqualTo(expectedUsername);
            assertThat(result.getPhoneNumber()).isEqualTo(phoneNumber);
            assertThat(result.getClientEmail()).isEqualTo(expectedEmail);
            assertThat(result.getStatus()).isEqualTo(Status.PENDING);
            assertThat(result.getAssignedProvider()).isEqualTo(assignedProvider);
            assertThat(result.getRequestDate()).isEqualTo(LocalDate.now());
        }
    }

    @Test
    void mapToProviderCallRequestResponse_ShouldMapAllFieldsCorrectly() {

        CallRequest callRequest = CallRequest.builder()
                .requestId(1L)
                .clientName("Test User")
                .phoneNumber("+1234567890")
                .clientEmail("test@example.com")
                .status(Status.PENDING)
                .assignedProvider("Provider1")
                .build();


        ProviderCallRequestResponse result = callRequestMapper.mapToProviderCallRequestResponse(callRequest);


        assertThat(result).isNotNull();
        assertThat(result.requestId()).isEqualTo(callRequest.getRequestId());
        assertThat(result.clientName()).isEqualTo(callRequest.getClientName());
        assertThat(result.phoneNumber()).isEqualTo(callRequest.getPhoneNumber());
        assertThat(result.clientEmail()).isEqualTo(callRequest.getClientEmail());
        assertThat(result.status()).isEqualTo(callRequest.getStatus().toString());
        assertThat(result.assignedProvider()).isEqualTo(callRequest.getAssignedProvider());
        assertThat(result.requestDate()).isEqualTo(callRequest.getRequestDate().toString());
    }
}
