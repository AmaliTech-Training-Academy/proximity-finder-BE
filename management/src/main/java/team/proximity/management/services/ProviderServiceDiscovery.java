package team.proximity.management.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import team.proximity.management.model.ProviderService;
import team.proximity.management.repositories.ProviderServiceRepository;

import java.util.List;

@Service
public class ProviderServiceDiscovery {

    private final ProviderServiceRepository repository;

    public ProviderServiceDiscovery(ProviderServiceRepository repository) {
        this.repository = repository;
    }

    public Page<ProviderService> searchByServiceAndProximity(String serviceName, double latitude, double longitude, double radius, Pageable pageable) {
        return repository.findByServiceNameAndLocationWithinRadiusNative(serviceName, latitude, longitude, radius, pageable);
    }
}

