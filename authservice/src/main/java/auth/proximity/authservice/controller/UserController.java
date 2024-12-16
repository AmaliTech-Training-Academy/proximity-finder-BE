package auth.proximity.authservice.controller;

import auth.proximity.authservice.dto.PaginatedResponse;
import auth.proximity.authservice.dto.RejectionEmailRequest;
import auth.proximity.authservice.dto.ResponseDto;
import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.services.impl.UserServiceImpl;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserServiceImpl userService;


    public UserController(UserServiceImpl userService) {
        this.userService = userService;
    }

    @GetMapping()
    public PaginatedResponse<User> getUsersByRole(@RequestParam AppRole role, Pageable pageable) {
        return userService.getUsersByRole(role, pageable);
    }
    @PutMapping("/{userId}/change-status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> changeStatus(@PathVariable Long userId, @RequestParam String status) {

        userService.changeUserStatus(userId,status);
        return ResponseEntity.ok(new ResponseDto("200", "Successfully changed user status to" + status));
    }
    @PostMapping("/send-rejection-email")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<ResponseDto> sendRejectionEmail(@RequestBody RejectionEmailRequest rejectionEmailRequest ) {

        userService.sendRejectionEmail(rejectionEmailRequest);
        return ResponseEntity.ok(new ResponseDto("200", "Successfully sent rejection email"));
    }
}
