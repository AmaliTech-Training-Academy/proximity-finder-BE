package auth.proximity.authservice.seeders;

import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import auth.proximity.authservice.enums.AccountStatus;
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
        if (!userRepository.existsByUserName("admin")) {
            User admin = new User("admin", "admin@gmail.com", passwordEncoder.encode("adminPass@1234"), "0243847248");
            admin.setAccountNonLocked(true);
            admin.setAccountNonExpired(true);
            admin.setCredentialsNonExpired(true);
            admin.setStatus(AccountStatus.ACTIVE);
            admin.setRole(adminRole);
            userRepository.save(admin);
        }
    }

}

