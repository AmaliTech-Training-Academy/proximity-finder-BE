package auth.proximity.authservice.service.impl;

import auth.proximity.authservice.dto.*;
import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.exception.ResourceNotFoundException;
import auth.proximity.authservice.exception.UserAlreadyExistsException;
import auth.proximity.authservice.repository.RoleRepository;
import auth.proximity.authservice.repository.UserRepository;
import auth.proximity.authservice.security.service.UserDetailsImpl;
import auth.proximity.authservice.service.IUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {


    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder encoder;

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
                foundUser.getBusinessAddress()
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
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
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
    public void updateProfilePicture(String email, ProfilePictureUpdateRequest profilePictureUpdateRequest) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email));

        user.setProfileImage(profilePictureUpdateRequest.file().getContentType());
        userRepository.save(user);
    }
    public void updateUserInfoByEmail(String email, UserUpdateRequest userUpdateRequest) {
        User foundUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email", email));
        if (userUpdateRequest.userName() != null) {
            foundUser.setUserName(userUpdateRequest.userName());
        }
        if (userUpdateRequest.phoneNumber() != null) {
            foundUser.setMobileNumber(userUpdateRequest.phoneNumber());
        }
        if (userUpdateRequest.businessOwnerName() != null) {
            foundUser.setBusinessOwnerName(userUpdateRequest.businessOwnerName());
        }
        if (userUpdateRequest.businessAddress() != null) {
            foundUser.setBusinessAddress(userUpdateRequest.businessAddress());
        }
        userRepository.save(foundUser);
    }
    public void deleteProfilePicture(String email) {
        User user = userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email));
        user.setProfileImage(null);
        userRepository.save(user);
    }
}


