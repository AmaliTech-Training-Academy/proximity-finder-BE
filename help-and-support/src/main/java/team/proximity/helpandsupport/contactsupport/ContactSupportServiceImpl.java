package team.proximity.helpandsupport.contactsupport;

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

@Service
public class ContactSupportServiceImpl implements ContactSupportService{
    private static final Logger LOGGER = LoggerFactory.getLogger(ContactSupportServiceImpl.class);

    @Value("${email.to-address}")
    private String toAddress;

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public ContactSupportServiceImpl(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void contactSupport(ContactSupportRequest request) {
        MimeMessage mimeMessage = createMimeMessage(request);
        sendEmail(mimeMessage);
    }

    private MimeMessage createMimeMessage(ContactSupportRequest request) {
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

            helper.setTo(toAddress);
            helper.setFrom(toAddress);
            helper.setReplyTo(request.email());
            helper.setSubject("New Support Request: " + request.subject());
            helper.setText(generateHtmlContent(request), true);

            return mimeMessage;
        } catch (MessagingException e) {
            LOGGER.error("Failed to create support email", e);
            throw new RuntimeException("Failed to create support email", e);
        }
    }

    private String generateHtmlContent(ContactSupportRequest request) {
        Context context = new Context();
        context.setVariable("userEmail", request.email());
        context.setVariable("userSubject", request.subject());
        context.setVariable("userMessage", request.message());
        return templateEngine.process("support-request-template", context);
    }

    private void sendEmail(MimeMessage mimeMessage) {
        try {
            mailSender.send(mimeMessage);
            LOGGER.info("Support email sent successfully to {}", toAddress);
        } catch (Exception e) {
            LOGGER.error("Failed to send support email", e);
            throw new RuntimeException("Failed to send support email", e);
        }
    }
}
