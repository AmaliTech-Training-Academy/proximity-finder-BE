package team.proximity.management.controllers;

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
    public List<ProviderService> searchByProximity(
            @RequestParam String serviceName,
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam double radius
    ) {
        return serviceDiscovery.searchByServiceAndProximity(serviceName, latitude, longitude, radius);
    }
}
