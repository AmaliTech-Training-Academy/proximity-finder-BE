package auth.proximity.authservice.services.security;

import auth.proximity.authservice.entity.PasswordResetToken;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.repository.PasswordResetTokenRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class TokenService {

    private final PasswordResetTokenRepository tokenRepository;

    public TokenService(PasswordResetTokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

    public String createPasswordResetToken(User user) {
        String token = UUID.randomUUID().toString();
        PasswordResetToken passwordResetToken = PasswordResetToken.builder()
                .user(user)
                .token(token)
                .expiryDate(LocalDateTime.now().plusSeconds(600))
                .build();

        tokenRepository.save(passwordResetToken);
        return token;
    }

    public PasswordResetToken validateToken(String token) {
        PasswordResetToken resetToken = tokenRepository.findByToken(token)
                .orElseThrow(() -> new IllegalArgumentException("Invalid token"));

        if (resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Token has expired");
        }
        return resetToken;
    }
}
