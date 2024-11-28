package team.proximity.request_management.request_management.request;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RequestRepository extends JpaRepository<Request, Long> {

    List<Request> findByAssignedProvider(String assignedProvider);
}
