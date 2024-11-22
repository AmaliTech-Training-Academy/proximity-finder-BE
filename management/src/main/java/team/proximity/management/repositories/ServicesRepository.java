package team.proximity.management.repositories;


import team.proximity.management.model.Services;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServicesRepository extends JpaRepository<Services, UUID> {
    Optional<Services> findByName(String name);
}

