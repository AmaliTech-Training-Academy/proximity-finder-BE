package auth.proximity.authservice.service.impl;

import auth.proximity.authservice.dto.UserDto;
import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.exception.ResourceNotFoundException;
import auth.proximity.authservice.exception.UserAlreadyExistsException;
import auth.proximity.authservice.repository.RoleRepository;
import auth.proximity.authservice.repository.UserRepository;
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

    /**
     * @param email - - Input User email address
     * @return User's Details based on a given email address
     */
    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(
                () -> new ResourceNotFoundException("User", "email", email)
        );
    }

    /**
     * @param userDto - UserDto Object
     */
    @Override
    public void createUser(UserDto userDto) {

        Optional<User> optionalUser = userRepository.findByEmail(userDto.getEmail());
        if (optionalUser.isPresent()) {
            throw new UserAlreadyExistsException("User already registered with given email: " + userDto.getEmail());
        }

        String roleStr = userDto.getRole();
        Role role = null;

        if (roleStr != null) {
            role = switch (roleStr) {
                case "admin" -> roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "role", roleStr));
                case "seeker" -> roleRepository.findByRoleName(AppRole.ROLE_SEEKER)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "role", roleStr));
                case "provider" -> roleRepository.findByRoleName(AppRole.ROLE_PROVIDER)
                        .orElseThrow(() -> new ResourceNotFoundException("User", "role", roleStr));
                default -> role;
            };
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

}
