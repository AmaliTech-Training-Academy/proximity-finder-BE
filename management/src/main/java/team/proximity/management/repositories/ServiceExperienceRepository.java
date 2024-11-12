package team.proximity.management.repositories;



import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import team.proximity.management.model.ServiceExperience;

@Repository
public interface ServiceExperienceRepository extends JpaRepository<ServiceExperience, Long> {
}

