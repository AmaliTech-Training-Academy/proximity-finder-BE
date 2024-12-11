package team.proximity.request_management.request_management.scheduling;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>{

    Optional<Event> findByEventIdAndCreatedBy(Long eventId, String createdBy);
    List<Event> findByCreatedBy(String createdBy);
}
