package auth.proximity.authservice.services;

import auth.proximity.authservice.dto.ForgotPasswordRequest;
import auth.proximity.authservice.dto.user.UserPasswordResetRequest;
import auth.proximity.authservice.entity.PasswordResetToken;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.repository.UserRepository;
import auth.proximity.authservice.services.security.TokenService;
import jakarta.mail.MessagingException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class PasswordService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService;

    public PasswordService(TokenService tokenService, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    public void resetPassword(String token, UserPasswordResetRequest request) {
        PasswordResetToken resetToken = tokenService.validateToken(token);

        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

    }

    public void initiatePasswordReset(ForgotPasswordRequest forgotPasswordRequest) {
        try {
            User user = userRepository.findByEmail(forgotPasswordRequest.email())
                    .orElseThrow(() -> new IllegalArgumentException("Staff with email " + forgotPasswordRequest.email() + " not found"));

            String token = tokenService.createPasswordResetToken(user);
            emailService.sendPasswordResetEmail(user, token);
        } catch (MessagingException e) {
            // Handle the exceptions as needed, e.g., log the error or rethrow a custom exception
            throw new RuntimeException("Failed to initiate password reset", e);
        }
    }
}
