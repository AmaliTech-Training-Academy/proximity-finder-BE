package team.proximity.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team.proximity.management.model.ProviderService;

import java.util.UUID;

public interface ProviderServiceRepository extends JpaRepository<ProviderService, UUID> {
}