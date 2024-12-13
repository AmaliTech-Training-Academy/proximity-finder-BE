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

@Service
public class EmailService {


    @Value("${email.from-address}")
    private String fromAddress;

    @Value("${email.subject}")
    private String subject;

    @Value("${email.reset-url}")
    private String resetUrl;

    private final Logger LOGGER = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    public EmailService(JavaMailSender mailSender, TemplateEngine templateEngine) {
        this.mailSender = mailSender;
        this.templateEngine = templateEngine;
    }

    public void sendPasswordResetEmail(User user, String token) throws MessagingException {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);

        helper.setTo(user.getEmail());
        helper.setFrom(fromAddress);
        helper.setSubject(subject);

        Context context = new Context();
        context.setVariable("name", user.getUserName());
        context.setVariable("resetUrl", resetUrl + token);

        String htmlContent = templateEngine.process("password-reset-template", context);
        helper.setText(htmlContent, true);

        mailSender.send(mimeMessage);

        LOGGER.info("Password reset email sent to: {}", user.getEmail());
        LOGGER.info("Password reset token: {}", token);

    }
}
