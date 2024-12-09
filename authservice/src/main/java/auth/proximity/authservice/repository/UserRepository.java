package auth.proximity.authservice.repository;

import auth.proximity.authservice.entity.AppRole;
import auth.proximity.authservice.entity.Role;
import auth.proximity.authservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
    Boolean existsByUserName(String username);
    Page<User> findByRoleRoleName(AppRole roleName, Pageable pageable);

}
