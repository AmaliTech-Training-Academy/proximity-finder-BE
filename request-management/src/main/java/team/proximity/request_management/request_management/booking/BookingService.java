package team.proximity.request_management.request_management.booking;

import java.util.List;

public interface BookingService {
    void completeBooking(Long bookingId);
    void declineBooking(Long bookingId);
    void acceptBooking(Long bookingId);
    void createBooking(BookingRequest request);
    List<BookingResponse> getBookingsForAssignedProvider();
    BookingResponse getBookingByIdForAssignedProvider(Long bookingId);
    List<BookingResponse> getBookingsForSeeker();
    BookingResponse getBookingByIdForCreatedBy(Long bookingId);
}
