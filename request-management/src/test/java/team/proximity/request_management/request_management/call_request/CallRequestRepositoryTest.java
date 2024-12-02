package team.proximity.request_management.request_management.call_request;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CallRequestRepositoryTest {

    @Autowired
    private CallRequestRepository callRequestRepository;

    private CallRequest callRequest1;
    private CallRequest callRequest2;
    private CallRequest callRequest3;

    @BeforeEach
    void setUp() {

        callRequestRepository.deleteAll();


        callRequest1 = CallRequest.builder()
                .clientName("John Doe")
                .phoneNumber("+1234567890")
                .clientEmail("john@example.com")
                .status(Status.PENDING)
                .assignedProvider("Provider1")
                .build();

        callRequest2 = CallRequest.builder()
                .clientName("Jane Smith")
                .phoneNumber("+1987654321")
                .clientEmail("jane@example.com")
                .status(Status.PENDING)
                .assignedProvider("Provider1")
                .build();

        callRequest3 = CallRequest.builder()
                .clientName("Bob Wilson")
                .phoneNumber("+1122334455")
                .clientEmail("bob@example.com")
                .status(Status.PENDING)
                .assignedProvider("Provider2")
                .build();


        callRequestRepository.saveAll(List.of(callRequest1, callRequest2, callRequest3));
    }

    @Test
    void findByAssignedProvider_ShouldReturnPageOfCallRequests() {

        Pageable pageable = PageRequest.of(0, 10);
        String provider = "Provider1";


        Page<CallRequest> result = callRequestRepository.findByAssignedProvider(provider, pageable);


        assertThat(result).isNotNull();
        assertThat(result.getContent()).hasSize(2);
        assertThat(result.getContent())
                .extracting(CallRequest::getAssignedProvider)
                .containsOnly(provider);
    }

    @Test
    void findByAssignedProvider_ShouldReturnEmptyPage_WhenProviderNotFound() {

        Pageable pageable = PageRequest.of(0, 10);
        String nonExistentProvider = "NonExistentProvider";


        Page<CallRequest> result = callRequestRepository.findByAssignedProvider(nonExistentProvider, pageable);


        assertThat(result).isNotNull();
        assertThat(result.getContent()).isEmpty();
    }

    @Test
    void findByRequestIdAndAssignedProvider_ShouldReturnCallRequest_WhenExists() {

        Long requestId = callRequest1.getRequestId();
        String provider = "Provider1";


        Optional<CallRequest> result = callRequestRepository.findByRequestIdAndAssignedProvider(requestId, provider);


        assertThat(result)
                .isPresent()
                .hasValueSatisfying(callRequest -> {
                    assertThat(callRequest.getRequestId()).isEqualTo(requestId);
                    assertThat(callRequest.getAssignedProvider()).isEqualTo(provider);
                });
    }

    @Test
    void findByRequestIdAndAssignedProvider_ShouldReturnEmpty_WhenNotExists() {

        Long nonExistentId = 999L;
        String provider = "Provider1";

        Optional<CallRequest> result = callRequestRepository.findByRequestIdAndAssignedProvider(nonExistentId, provider);

        assertThat(result).isEmpty();
    }

    @Test
    void findByPhoneNumberAndAssignedProvider_ShouldReturnCallRequest_WhenExists() {

        String phoneNumber = "+1234567890";
        String provider = "Provider1";

        Optional<CallRequest> result = callRequestRepository.findByPhoneNumberAndAssignedProvider(phoneNumber, provider);

        assertThat(result).isPresent();
    }
}