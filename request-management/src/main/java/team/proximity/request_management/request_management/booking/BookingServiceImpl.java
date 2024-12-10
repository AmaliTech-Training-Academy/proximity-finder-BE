package team.proximity.request_management.request_management.booking;

import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.util.List;

@Service
public class BookingServiceImpl implements BookingService{
    private final Logger LOGGER = LoggerFactory.getLogger(BookingServiceImpl.class);

    @Value("${spring.rabbitmq.exchange.name}")
    private String exchangeName;

    @Value("${spring.rabbitmq.routing.key}")
    private String routingKey;

    private final BookingRepository bookingRepository;
    private final BookingMapper bookingMapper;
    private final RabbitTemplate rabbitTemplate;

    public BookingServiceImpl(BookingRepository bookingRepository, BookingMapper bookingMapper, RabbitTemplate rabbitTemplate) {
        this.bookingRepository = bookingRepository;
        this.bookingMapper = bookingMapper;
        this.rabbitTemplate = rabbitTemplate;
    }

    public void createBooking(BookingRequest request) {
        LOGGER.info("Creating new booking from request: {}", request);
        Booking booking = bookingMapper.toBooking(request);
        bookingRepository.save(booking);
        LOGGER.info("Successfully created booking with ID: {}", booking.getBookingId());
    }

    public void acceptBooking(Long bookingId) {
        LOGGER.info("Attempting to accept booking with ID: {}", bookingId);
        Booking booking = getBookingByIdAndAssignedProvider(bookingId);
        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
            LOGGER.warn("Cannot accept booking {}. Current status: {}", bookingId, booking.getStatus());
            throw new IllegalStateException("Only PENDING bookings can be accepted.");
        }
        booking.setStatus(BookingStatus.ACCEPTED);
        bookingRepository.save(booking);
        LOGGER.info("Successfully accepted booking with ID: {}", bookingId);
        publishBookingStatusChangedEvent(booking);
    }

    public void declineBooking(Long bookingId) {
        LOGGER.info("Attempting to decline booking with ID: {}", bookingId);
        Booking booking = getBookingByIdAndAssignedProvider(bookingId);
        if (!BookingStatus.PENDING.equals(booking.getStatus())) {
            LOGGER.warn("Cannot decline booking {}. Current status: {}", bookingId, booking.getStatus());
            throw new IllegalStateException("Only PENDING bookings can be declined.");
        }
        booking.setStatus(BookingStatus.DECLINED);
        bookingRepository.save(booking);
        LOGGER.info("Successfully declined booking with ID: {}", bookingId);
        publishBookingStatusChangedEvent(booking);
    }

    public void completeBooking(Long bookingId) {
        LOGGER.info("Attempting to complete booking with ID: {}", bookingId);
        Booking booking = getBookingByIdAndAssignedProvider(bookingId);
        if (!BookingStatus.ACCEPTED.equals(booking.getStatus())) {
            LOGGER.warn("Cannot complete booking {}. Current status: {}", bookingId, booking.getStatus());
            throw new IllegalStateException("Only ACCEPTED bookings can be marked as COMPLETED.");
        }
        booking.setStatus(BookingStatus.COMPLETED);
        bookingRepository.save(booking);
        LOGGER.info("Successfully completed booking with ID: {}", bookingId);
        publishBookingStatusChangedEvent(booking);
    }

    public List<BookingResponse> getBookingsForAssignedProvider() {
        String provider = SecurityContextUtils.getEmail();
        LOGGER.info("Fetching all bookings for provider: {}", provider);
        return bookingRepository.findByAssignedProvider(provider)
            .stream()
            .map(bookingMapper::toBookingResponse)
            .toList();
    }

    public BookingResponse getBookingByIdForAssignedProvider(Long bookingId) {
        LOGGER.info("Fetching booking details for ID: {}", bookingId);
        return bookingMapper.toBookingResponse(
            bookingRepository.findByBookingIdAndAssignedProvider(bookingId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Booking not found or you are not authorized to view it."))
        );
    }

    public List<BookingResponse> getBookingsForSeeker() {
        String seeker = SecurityContextUtils.getEmail();
        LOGGER.info("Fetching all bookings for seeker: {}", seeker);
        return bookingRepository.findByCreatedBy(seeker)
                .stream()
                .map(bookingMapper::toBookingResponse)
                .toList();
    }
    public BookingResponse getBookingByIdForCreatedBy(Long bookingId) {
        LOGGER.info("Fetching booking details for seeker: {}", SecurityContextUtils.getEmail());
        return bookingMapper.toBookingResponse(
                bookingRepository.findByBookingIdAndCreatedBy(bookingId, SecurityContextUtils.getEmail())
                        .orElseThrow(() -> new EntityNotFoundException("Booking not found or you are not authorized to view it."))
        );
    }


    private Booking getBookingByIdAndAssignedProvider(Long bookingId) {
        String currentProvider = SecurityContextUtils.getEmail();
        LOGGER.info("Fetching booking {} for provider: {}", bookingId, currentProvider);
        return bookingRepository.findByBookingIdAndAssignedProvider(bookingId, currentProvider)
                .orElseThrow(() -> {
                    LOGGER.error("Unauthorized access attempt to booking {} by provider: {}", bookingId, currentProvider);
                    return new SecurityException("You are not authorized to perform actions on this booking.");
                });
    }


    private void publishBookingStatusChangedEvent(Booking booking) {
        LOGGER.info("Publishing status change event for booking: {}", booking.getBookingId());
        BookingStatusChangedEvent event = new BookingStatusChangedEvent(
                booking.getBookingId(), booking.getStatus().toString(), booking.getCreatedBy()
        );
        try {
            rabbitTemplate.convertAndSend(exchangeName, routingKey, event);
            LOGGER.info("Successfully published status change event for booking: {}", booking.getBookingId());
        } catch (Exception e) {
            LOGGER.error("Failed to publish status change event for booking: {}. Error: {}", booking.getBookingId(), e.getMessage());
            throw e;
        }
    }
}