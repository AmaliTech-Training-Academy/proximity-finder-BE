package team.proximity.request_management.request_management.booking;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.quotes.ApiSuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quote-service/bookings")
public class BookingController {

    private final BookingService bookingService;

    public BookingController(BookingServiceImpl bookingService) {
        this.bookingService = bookingService;
    }

    @PostMapping
    public ResponseEntity<ApiSuccessResponse> createBooking(@RequestBody @Valid BookingRequest request) {
        bookingService.createBooking(request);
        return new ResponseEntity<>(
                new ApiSuccessResponse("Booking created successfully"),
                HttpStatus.CREATED
        );
    }
    @GetMapping()
    public ResponseEntity<List<BookingResponse>> getBookingsForAssignedProvider() {
        return ResponseEntity.ok(
                bookingService.getBookingsForAssignedProvider()
        );
    }
    @GetMapping("/{bookingId}/provider")
    public ResponseEntity<BookingResponse> getBookingById(@PathVariable Long bookingId) {
        return ResponseEntity.ok(
                bookingService.getBookingByIdForAssignedProvider(bookingId)
        );
    }

    @PutMapping("/{bookingId}/accept")
    public ResponseEntity<ApiSuccessResponse> acceptBooking(@PathVariable Long bookingId) {
        bookingService.acceptBooking(bookingId);
        return ResponseEntity.ok(
                new ApiSuccessResponse("Booking accepted successfully")
        );
    }

    @PutMapping("/{bookingId}/decline")
    public ResponseEntity<ApiSuccessResponse> declineBooking(@PathVariable Long bookingId) {
        bookingService.declineBooking(bookingId);
        return ResponseEntity.ok(
                new ApiSuccessResponse("Booking declined successfully")
        );
    }

    @PutMapping("/{bookingId}/complete")
    public ResponseEntity<ApiSuccessResponse> completeBooking(@PathVariable Long bookingId) {
        bookingService.completeBooking(bookingId);
        return ResponseEntity.ok(
                new ApiSuccessResponse("Booking completed successfully")
        );
    }

    @GetMapping()
    public ResponseEntity<List<BookingResponse>> getBookingsCreatedBy() {
        return ResponseEntity.ok(
                bookingService.getBookingsCreatedBy()
        );
    }

    @GetMapping("/{bookingId}/seeker")
    public ResponseEntity<BookingResponse> getBookingByIdForCreatedBy(@PathVariable Long bookingId) {
        return ResponseEntity.ok(
                bookingService.getBookingByIdForCreatedBy(bookingId)
        );
    }

}
