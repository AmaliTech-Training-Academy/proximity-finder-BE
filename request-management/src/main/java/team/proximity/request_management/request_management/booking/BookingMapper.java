package team.proximity.request_management.request_management.booking;

import org.springframework.stereotype.Component;
import team.proximity.request_management.request_management.security.SecurityContextUtils;
@Component
public class BookingMapper {

    public Booking toBooking(BookingRequest request) {
        Booking booking = new Booking();
        booking.setStartDate(request.startDate());
        booking.setStartTime(request.startTime());
        booking.setEndDate(request.endDate());
        booking.setEndTime(request.endTime());
        booking.setDescription(request.description());
        booking.setAssignedProvider(request.assignedProvider());
        booking.setStatus(BookingStatus.PENDING);
        booking.setCreatedBy(SecurityContextUtils.getEmail());
        return booking;
    }


    public BookingResponse toBookingResponse(Booking booking) {
        return new BookingResponse(
                booking.getBookingId(),
                booking.getStartDate(),
                booking.getStartTime(),
                booking.getEndDate(),
                booking.getEndTime(),
                booking.getDescription(),
                booking.getCreatedBy(),
                booking.getAssignedProvider(),
                booking.getStatus().toString()

        );
    }

}
