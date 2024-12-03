package team.proximity.management.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import team.proximity.management.model.ProviderService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ProviderServiceRepository extends JpaRepository<ProviderService, UUID> {
   Optional<List<ProviderService>> findByUserEmail(String email);
   @Query(value = """
        SELECT ps.* FROM provider_service ps
        JOIN services s ON ps.service_id = s.id
        WHERE s.name = :serviceName 
        AND ST_DWithin(ps.location, ST_GeographyFromText(concat('POINT(', :longitude, ' ', :latitude, ')')), :radius)
    """, nativeQuery = true)
   Page<ProviderService> findByServiceNameAndLocationWithinRadiusNative(
           @Param("serviceName") String serviceName,
           @Param("latitude") double latitude,
           @Param("longitude") double longitude,
           @Param("radius") double radius,
           Pageable pageable
   );
}