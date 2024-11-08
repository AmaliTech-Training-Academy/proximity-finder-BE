package team.proximity.management.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import team.proximity.management.model.Preference;

import java.util.UUID;

public interface PreferenceRepository extends JpaRepository<Preference, UUID> {
}