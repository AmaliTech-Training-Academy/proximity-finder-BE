package auth.proximity.authservice.services.impl;

import auth.proximity.authservice.dto.*;
import auth.proximity.authservice.dto.user.AdminUpdatePasswordRequest;
import auth.proximity.authservice.dto.user.UserDto;
import auth.proximity.authservice.dto.user.UserInfoResponse;
import auth.proximity.authservice.dto.user.UserUpdateRequest;
import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.enums.AccountStatus;
import auth.proximity.authservice.exception.ResourceNotFoundException;
import auth.proximity.authservice.exception.UserAlreadyExistsException;
import auth.proximity.authservice.repository.RoleRepository;
import auth.proximity.authservice.repository.UserRepository;
import auth.proximity.authservice.security.AuthenticationHelper;
import auth.proximity.authservice.services.EmailService;
import auth.proximity.authservice.services.IUserService;
import auth.proximity.authservice.services.security.UserDetailsImpl;
import jakarta.mail.MessagingException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;
    private final EmailService emailService;

    public UserInfoResponse getUserInfo(String email) {
        User foundUser = userRepository.findByEmail(email).orElseThrow(()->
                new ResourceNotFoundException("User", "email", email));
        return new UserInfoResponse(
                foundUser.getUserId(),
                foundUser.getUserName(),
                foundUser.getEmail(),
                foundUser.getMobileNumber(),
                foundUser.getBusinessOwnerName(),
                foundUser.getProfileImage(),
//                foundUser.getBusinessAddress(),
                foundUser.getPlaceName(),
                foundUser.getLongitude(),
                foundUser.getLatitude(),
                foundUser.getRole(),
                foundUser.getStatus()
        );
    }


    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );
    }

    @Override
    public void createUser(UserDto userDto) {

        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with given email: " + userDto.getEmail());
        }

        RequestRole roleStr = userDto.getRole();
        Role role = null;

        if (roleStr != null) {
            switch (roleStr.toString()) {
                case "ADMIN" -> role = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "role", roleStr.toString()));
                case "SEEKER" -> role = roleRepository.findByRoleName(AppRole.ROLE_SEEKER)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "role", roleStr.toString()));
                case "PROVIDER" -> role = roleRepository.findByRoleName(AppRole.ROLE_PROVIDER)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "role", roleStr.toString()));
                default -> {
                    throw new ResourceNotFoundException("Role", "role", roleStr.toString());
                }
            }
        } else {
            throw new ResourceNotFoundException("Role", "role", null);
        }

        User user = User.builder()
                .userName(userDto.getUserName())
                .email(userDto.getEmail())
                .mobileNumber(userDto.getMobileNumber())
                .password(encoder.encode(userDto.getPassword()))
                .businessOwnerName(userDto.getBusinessOwnerName())
//                .businessAddress(userDto.getBusinessAddress())
                .placeName(userDto.getPlaceName())
                .latitude(userDto.getLatitude())
                .longitude(userDto.getLongitude())
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .status(AccountStatus.PENDING)
                .role(role)
                .build();

        userRepository.save(user);
    }

    public  void updatePassword(String email, AdminUpdatePasswordRequest adminUpdatePasswordRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );
        if (user.getPassword() != null && !encoder.matches(adminUpdatePasswordRequest.oldPassword(), user.getPassword())) {
            throw new ResourceNotFoundException("User", "password", adminUpdatePasswordRequest.oldPassword());
        }
        if(adminUpdatePasswordRequest.oldPassword().equals(adminUpdatePasswordRequest.newPassword())){
            throw new ResourceNotFoundException("User", "password", "Old password and new password cannot be same");
        }
        if(adminUpdatePasswordRequest.confirmPassword() != null && !adminUpdatePasswordRequest.newPassword().equals(adminUpdatePasswordRequest.confirmPassword())){
            throw new ResourceNotFoundException("User", "password", "New password and confirm password should be same");
        }
        user.setPassword(encoder.encode(adminUpdatePasswordRequest.newPassword()));
        userRepository.save(user);
    }
    public void updateUserInfoByEmail(String email, UserUpdateRequest userUpdateRequest) {
        if ((userUpdateRequest.userName() == null || userUpdateRequest.userName().isEmpty()) &&
                (userUpdateRequest.mobileNumber() == null || userUpdateRequest.mobileNumber().isEmpty()) &&
                (userUpdateRequest.businessOwnerName() == null || userUpdateRequest.businessOwnerName().isEmpty()) &&
                (userUpdateRequest.placeName() == null || userUpdateRequest.placeName().isEmpty())) {
            throw new IllegalArgumentException("At least one non-empty field must be provided for update");
        }

        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        if (userUpdateRequest.userName() != null) {
            foundUser.setUserName(userUpdateRequest.userName());
        }
        if (userUpdateRequest.mobileNumber() != null) {
            foundUser.setMobileNumber(userUpdateRequest.mobileNumber());
        }
        if (userUpdateRequest.businessOwnerName() != null) {
            foundUser.setBusinessOwnerName(userUpdateRequest.businessOwnerName());
        }
        if (userUpdateRequest.placeName() != null) {
            foundUser.setPlaceName(userUpdateRequest.placeName());
        }
        if (userUpdateRequest.latitude() != 0.0) {
            foundUser.setLatitude(userUpdateRequest.latitude());
        }
        if (userUpdateRequest.longitude() != 0.0) {
            foundUser.setLongitude(userUpdateRequest.longitude());
        }
        userRepository.save(foundUser);
    }
    public void deleteProfilePicture(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email));
        user.setProfileImage(null);
        userRepository.save(user);
    }
    public PaginatedResponse<User> getUsersByRole(AppRole role, Pageable pageable) {
        Page<User> users = userRepository.findByRoleRoleName(role, pageable);
        PaginatedResponse.PaginationMetadata metadata = new PaginatedResponse.PaginationMetadata(
                users.getNumber(),    // current page
                users.getTotalPages(), // total pages
                users.getTotalElements(), // total elements
                users.getSize()        // page size
        );
        return new PaginatedResponse<>(users.getContent(), metadata);
    }
    @Override
    public void changeUserStatus(Long userId, String status) {
        AccountStatus accountStatus = AccountStatus.valueOf(status.toUpperCase());
        User user = userRepository.findById(userId).orElseThrow(
                () -> new ResourceNotFoundException("User", "id", userId.toString()));
        user.setStatus(accountStatus);
        userRepository.save(user);
    }
    @Override
    public void sendRejectionEmail(RejectionEmailRequest rejectionEmailRequest) {
        try {
            User user = userRepository.findByEmail(rejectionEmailRequest.email()).orElseThrow(
                    () -> new ResourceNotFoundException("User", "id", rejectionEmailRequest.email()));
            emailService.sendRejectionEmail(user, rejectionEmailRequest.reason());
        } catch (MessagingException e) {
            // Handle the exception, e.g., log the error or rethrow a custom exception
            throw new RuntimeException("Failed to send rejection email", e);
        }
    }
}


