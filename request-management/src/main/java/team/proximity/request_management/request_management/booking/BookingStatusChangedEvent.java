package team.proximity.request_management.request_management.booking;

import java.io.Serializable;

public record BookingStatusChangedEvent(
         Long bookingId,
         String status,
         String seekerEmail
) implements Serializable {}
