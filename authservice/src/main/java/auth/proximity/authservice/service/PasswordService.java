package auth.proximity.authservice.service;

import auth.proximity.authservice.dto.UserPasswordResetRequest;
import auth.proximity.authservice.entity.PasswordResetToken;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.repository.UserRepository;
import auth.proximity.authservice.security.service.TokenService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class PasswordService {
    private final TokenService tokenService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
//    private final EmailService emailService;

    public PasswordService(TokenService tokenService, UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
//        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
    }


    public String resetPassword(String token, UserPasswordResetRequest request) {
        PasswordResetToken resetToken = tokenService.validateToken(token);

        if (!request.password().equals(request.confirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.password()));
        userRepository.save(user);

        return "Password reset successful";
    }

    public void initiatePasswordReset(ResetRequest email) throws MessagingException, IOException {
        Staff staff = userRepository.findByEmail(email.staffEmail())
                .orElseThrow(() -> new StaffNotFoundException("Staff with email " + email + " not found"));

        String token = tokenService.createPasswordResetToken(staff);
        emailService.sendPasswordResetEmail(staff,token);
    }
}
