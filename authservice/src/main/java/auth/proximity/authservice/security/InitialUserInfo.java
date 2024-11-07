package auth.proximity.authservice.security;

import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.repository.RoleRepository;
import auth.proximity.authservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class InitialUserInfo implements CommandLineRunner {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        Role seeker = roleRepository.findByRoleName(AppRole.ROLE_SEEKER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_SEEKER)));
        Role provider = roleRepository.findByRoleName(AppRole.ROLE_PROVIDER)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_PROVIDER)));
        Role adminRole = roleRepository.findByRoleName(AppRole.ROLE_ADMIN)
                .orElseGet(() -> roleRepository.save(new Role(AppRole.ROLE_ADMIN)));
        if (!userRepository.existsByUserName("seeker")) {
            User user1 = new User("seeker", "seeker@gmail.com", passwordEncoder.encode("password1"), "0243847248");
            user1.setAccountNonLocked(false);
            user1.setAccountNonExpired(true);
            user1.setCredentialsNonExpired(true);
            user1.setEnabled(true);
            user1.setRole(seeker);
            userRepository.save(user1);
        }
        if (!userRepository.existsByUserName("provider")) {
            User user1 = new User("provider", "provider@gmail.com", passwordEncoder.encode("password1"), "0243847248");
            user1.setAccountNonLocked(false);
            user1.setAccountNonExpired(true);
            user1.setCredentialsNonExpired(true);
            user1.setEnabled(true);
            user1.setRole(provider);
            userRepository.save(user1);
        }
        if (!userRepository.existsByUserName("admin")) {
            User admin = new User("admin", "admin@gmail.com", passwordEncoder.encode("adminPass"), "0243847248");
            admin.setAccountNonLocked(true);
            admin.setAccountNonExpired(true);
            admin.setCredentialsNonExpired(true);
            admin.setEnabled(true);
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
    }
}
