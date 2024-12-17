package team.proximity.request_management.request_management.scheduling;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long>{

    Optional<Event> findByEventIdAndCreatedBy(Long eventId, String createdBy);
    List<Event> findByCreatedBy(String createdBy);
    @Query("SELECT e FROM Event e WHERE :schedulingDate BETWEEN e.startDate AND e.endDate AND e.createdBy = :createdBy")
    List<Event> findEventsOnDate(
            @Param("schedulingDate") String schedulingDate,
            @Param("createdBy") String createdBy
    );
    @Query("""
    SELECT COUNT(e) > 0 
    FROM Event e 
    WHERE e.startDate = :startDate 
      AND e.startTime < :endTime 
      AND e.endTime > :startTime
    """)
    boolean existsByStartDateAndTimeRange(String startDate, String startTime, String endTime);

}
