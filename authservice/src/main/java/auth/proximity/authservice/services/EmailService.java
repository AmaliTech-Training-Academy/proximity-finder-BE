package auth.proximity.authservice.services;

import auth.proximity.authservice.entity.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.util.Map;
import java.util.HashMap;

@Service
public class EmailService {

    @Value("${email.from-address}")
    private String fromAddress;

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    /**
     * Generic method to send emails using Thymeleaf templates
     *
     * @param user Recipient user
     * @param subject Email subject
     * @param templateName Name of the Thymeleaf template
     * @param templateVariables Additional variables for the template
     * @throws MessagingException if there's an error sending the email
     */
    public void sendTemplatedEmail(
            User user,
            String subject,
            String templateName,
            Map<String, Object> templateVariables
    ) throws MessagingException {
        // Create mime message
        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        // Set email details
        helper.setTo(user.getEmail());
        helper.setFrom(fromAddress);
        helper.setSubject(subject);

        // Prepare template context
        Context context = new Context();

        // Always include user's name as a default variable
        context.setVariable("name", user.getUserName());

        // Add any additional template variables
        if (templateVariables != null) {
            templateVariables.forEach(context::setVariable);
        }

        // Process template
        String htmlContent = templateEngine.process(templateName, context);
        helper.setText(htmlContent, true);

        // Send email
        mailSender.send(mimeMessage);

        LOGGER.info("Email sent to: {} with template: {}", user.getEmail(), templateName);
    }

    /**
     * Send a password reset email
     *
     * @param user User requesting password reset
     * @param token Password reset token
     * @throws MessagingException if there's an error sending the email
     */
    public void sendPasswordResetEmail(User user, String token) throws MessagingException {
        // Create context variables for password reset template
        Map<String, Object> variables = new HashMap<>();
        variables.put("resetUrl", token);

        // Send email using the generic method
        sendTemplatedEmail(
                user,
                "Password Reset",
                "password-reset-template",
                variables
        );

        LOGGER.info("Password reset token logged for: {}", user.getEmail());
    }

    /**
     * Send a rejection email
     *
     * @param user User receiving rejection
     * @param reason Reason for rejection
     * @throws MessagingException if there's an error sending the email
     */
    public void sendRejectionEmail(User user, String reason) throws MessagingException {
        // Create context variables for rejection email
        Map<String, Object> variables = new HashMap<>();
        variables.put("reason", reason);

        // Send email using the generic method
        sendTemplatedEmail(
                user,
                "Account Application Update",
                "rejection-email",
                variables
        );

        LOGGER.info("Rejection email sent to: {}", user.getEmail());
    }
}