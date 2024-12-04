package team.proximity.management.seeders;

import jakarta.annotation.PostConstruct;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import team.proximity.management.model.ProviderService;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ProviderServiceRepository;
import team.proximity.management.repositories.ServicesRepository;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Component
public class ProviderSeeder {
    @Autowired
    private ProviderServiceRepository providerServiceRepository;

    @Autowired
    private ServicesRepository servicesRepository;

    public void seedProviderServices() {
        GeometryFactory geometryFactory = new GeometryFactory();

        // Ensure services exist first
        List<Services> services = servicesRepository.findAll();
        if (services.isEmpty()) {
            throw new IllegalStateException("Services must be seeded first");
        }

        List<ProviderService> providerServices = Arrays.asList(
                ProviderService.builder()
                        .userEmail("hmedzubairu365@gmail.com")
                        .service(services.get(0)) // First service
                        .paymentPreference("CASH")
                        .location(geometryFactory.createPoint(new Coordinate(6.656120, -1.628033))) // New York City
                        .placeName("Ahodwo")
                        .schedulingPolicy("FLEXIBLE")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ProviderService.builder()
                        .userEmail("hmedzubairu365@gmail.com")
                        .service(services.get(1)) // Second service
                        .paymentPreference("BANK_TRANSFER")


                .location(geometryFactory.createPoint(new Coordinate(6.656120, -1.628033))) // San Francisco
                        .placeName("Ahodwo")
                        .schedulingPolicy("STRICT")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ProviderService.builder()
                        .userEmail("hmedzubairu365@gmail.com")
                        .service(services.get(2)) // Third service
                        .paymentPreference("CREDIT_CARD")
                        .location(geometryFactory.createPoint(new Coordinate(6.656120, -1.628033))) // Los Angeles
                        .schedulingPolicy("MODERATE")
                        .placeName("Ahodwo")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ProviderService.builder()
                        .userEmail("hmedzubairu365@gmail.com")
                        .service(services.get(3)) // Fourth service
                        .paymentPreference("PAYPAL")
                        .location(geometryFactory.createPoint(new Coordinate(5.5268018, -0.4780016))) // Chicago
                        .schedulingPolicy("FLEXIBLE")
                        .placeName("Kasoa")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build(),

                ProviderService.builder()
                        .userEmail("hmedzubairu365@gmail.com")
                        .service(services.get(4)) // Fifth service
                        .paymentPreference("CRYPTO")
                        .location(geometryFactory.createPoint(new Coordinate(5.5268018, -0.4780016))) // Miami
                        .schedulingPolicy("STRICT")
                        .createdAt(LocalDateTime.now())
                        .updatedAt(LocalDateTime.now())
                        .build()
        );

        providerServiceRepository.saveAll(providerServices);
    }

//    @PostConstruct
//    public void init() {
//        seedProviderServices();
//    }
}
