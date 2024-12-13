package team.proximity.notification.notification_service.bookings;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import team.proximity.notification.notification_service.email.EmailService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import team.proximity.notification.notification_service.exceptions.NotificationProcessingException;

@Service
public class BookingsNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(BookingsNotificationListener.class);
    private final EmailService emailService;

    @Autowired
    public BookingsNotificationListener(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = "${spring.rabbitmq.queue.name}")
    public void handleBookingStatusChangedEvent(BookingStatusChangedEvent event) {
        log.info("Received booking status change event for bookingId: {}", event.bookingId());

        try {
            Context context = buildEmailContext(event);
            emailService.sendEmail(
                event.seekerEmail(),
                generateEmailSubject(event.status()),
                "email-template",
                context
            );
            log.debug("Email notification sent successfully for bookingId: {}", event.bookingId());
        } catch (Exception e) {
            log.error("Failed to process booking notification for bookingId: {}", event.bookingId(), e);
            throw new NotificationProcessingException("Failed to process booking notification");
        }
    }

    private Context buildEmailContext(BookingStatusChangedEvent event) {
        Context context = new Context();
        context.setVariable("userName", "User");
        context.setVariable("bookingId", event.bookingId());
        context.setVariable("status", event.status());
        context.setVariable("message", generateEmailBodyMessage(event));
        return context;
    }

    private String generateEmailSubject(String status) {
        return switch (status) {
            case "PENDING" -> "Booking Request Received";
            case "ACCEPTED" -> "Booking Request Accepted!";
            case "DECLINED" -> "Booking Request Declined";
            case "COMPLETED" -> "Booking Completed Successfully";
            default -> "Booking Status Updated";
        };
    }

    private String generateEmailBodyMessage(BookingStatusChangedEvent event) {
        return switch (event.status()) {
            case "PENDING" -> "Your booking request is under review.";
            case "ACCEPTED" -> "We're excited to serve you soon!";
            case "DECLINED" -> "We're sorry your request was declined.";
            case "COMPLETED" -> "Thank you for using our service!";
            default -> "Your booking status has been updated.";
        };
    }
}