package team.proximity.notification.notification_service.bookings;

public record BookingStatusChangedEvent(
        Long bookingId,
        String status,
        String seekerEmail
) {}




