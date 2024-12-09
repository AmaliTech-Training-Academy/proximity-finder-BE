package auth.proximity.authservice.controller;

import auth.proximity.authservice.dto.ForgotPasswordRequest;
import auth.proximity.authservice.dto.ResponseDto;
import auth.proximity.authservice.dto.user.UserPasswordResetRequest;
import auth.proximity.authservice.services.PasswordService;
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
    public ResponseEntity<ResponseDto> resetPassword(@PathVariable("token") String token, @RequestBody UserPasswordResetRequest userPasswordResetRequest) {
        passwordService.resetPassword(token, userPasswordResetRequest);
        return new ResponseEntity<>( new ResponseDto("200", "Successfully updated"), HttpStatus.OK);
    }


    @PostMapping("/reset-request")
    public ResponseEntity<ResponseDto> initiatePasswordReset(@RequestBody ForgotPasswordRequest forgotPasswordRequest) {

            passwordService.initiatePasswordReset(forgotPasswordRequest);
            return ResponseEntity.ok(new ResponseDto("200", "Password reset initiated successfully"));


    }

}
