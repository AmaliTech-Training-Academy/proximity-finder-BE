package team.proximity.request_management.request_management.booking;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findByBookingIdAndAssignedProvider(Long bookingId, String assignedProvider);
    List<Booking> findByAssignedProvider(String assignedProvider);

    Optional<Booking> findByBookingIdAndCreatedBy(Long bookingId, String createdBy);

    List<Booking> findByCreatedBy(String createdBy);
}

