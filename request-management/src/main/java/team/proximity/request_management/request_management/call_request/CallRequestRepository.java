package team.proximity.request_management.request_management.call_request;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CallRequestRepository extends JpaRepository<CallRequest, Long> {

    Page<CallRequest> findByAssignedProvider(String assignedProvider, Pageable pageable);
    Optional<CallRequest> findByRequestIdAndAssignedProvider(Long requestId, String assignedProvider);
    Optional<CallRequest> findByPhoneNumberAndAssignedProvider(String phoneNumber, String assignedProvider);
}
