package auth.proximity.authservice.controller;

import auth.proximity.authservice.dto.ForgotPasswordRequest;
import auth.proximity.authservice.dto.ResponseDto;
import auth.proximity.authservice.dto.UserPasswordResetRequest;
import auth.proximity.authservice.service.PasswordService;
import jakarta.mail.MessagingException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/api/v1/password")
public class PasswordController {
    private final PasswordService passwordService;

    public PasswordController(PasswordService passwordService) {
        this.passwordService = passwordService;
    }


    @PostMapping("/reset-password/{token}")
    public ResponseEntity<String> resetPassword(@PathVariable("token") String token, @RequestBody UserPasswordResetRequest userPasswordResetRequest) {
        passwordService.resetPassword(token, userPasswordResetRequest);
        return new ResponseEntity<>("Password reset successful", HttpStatus.OK);
    }

    // Endpoint to initiate a password reset
    @PostMapping("/reset-request")
    public ResponseEntity<String> initiatePasswordReset(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {
        try {
            passwordService.initiatePasswordReset(forgotPasswordRequest);
            return ResponseEntity.ok("Password reset email sent successfully");
        } catch (MessagingException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred while sending the email");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
