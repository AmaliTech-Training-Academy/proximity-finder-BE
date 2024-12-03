package team.proximity.management.controllers;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;
import team.proximity.management.model.ProviderService;
import team.proximity.management.services.ProviderServiceDiscovery;

import java.util.List;

@RestController
@RequestMapping("/api/v1/service-discovery")
public class SearchDiscoveryControler {

    private final ProviderServiceDiscovery serviceDiscovery;


    public SearchDiscoveryControler(ProviderServiceDiscovery serviceDiscovery) {
        this.serviceDiscovery = serviceDiscovery;
    }

    @GetMapping("/search")
    public Page<ProviderService> searchByProximity(
            @RequestParam String serviceName,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius,
            Pageable pageable
    ) {
        return serviceDiscovery.searchByServiceAndProximity(serviceName, latitude, longitude, radius, pageable);
    }
}
