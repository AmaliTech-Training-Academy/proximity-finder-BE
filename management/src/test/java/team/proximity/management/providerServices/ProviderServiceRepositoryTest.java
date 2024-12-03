//package team.proximity.management.providerServices;
//
//
//import org.junit.jupiter.api.Test;
//import org.locationtech.jts.geom.GeometryFactory;
//import org.locationtech.jts.geom.Point;
//import org.locationtech.jts.geom.PrecisionModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.PageRequest;
//import team.proximity.management.model.ProviderService;
//import team.proximity.management.model.Services;
//import team.proximity.management.repositories.ProviderServiceRepository;
//
//import java.util.List;
//import java.util.Optional;
//import java.util.UUID;
//
//import static org.assertj.core.api.Assertions.assertThat;
//
//@DataJpaTest
//class ProviderServiceRepositoryTest {
//
//    @Autowired
//    private ProviderServiceRepository repository;
//
//    @Test
//    void shouldFindProviderServicesByUserEmail() {
//        // Arrange
//        String email = "test@example.com";
//        Services service = Services.builder().id(UUID.randomUUID()).name("Test Service").build();
//        ProviderService providerService = ProviderService.builder()
//                .userEmail(email)
//                .service(service)
//                .build();
//        repository.save(providerService);
//
//        // Act
//        Optional<List<ProviderService>> result = repository.findByUserEmail(email);
//
//        // Assert
//        assertThat(result).isPresent();
//        assertThat(result.get()).hasSize(1);
//        assertThat(result.get().get(0).getUserEmail()).isEqualTo(email);
//    }
//
//    @Test
//    void shouldFindProviderServicesWithinRadius() {
//        // Arrange
//        String serviceName = "Test Service";
//        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
//        Point location1 = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(12.9716, 77.5946)); // Bangalore
//        Point location2 = geometryFactory.createPoint(new org.locationtech.jts.geom.Coordinate(13.0827, 80.2707)); // Chennai
//
//        Services service = Services.builder().id(UUID.randomUUID()).name(serviceName).build();
//
//        ProviderService provider1 = ProviderService.builder()
//                .service(service)
//                .location(location1)
//                .build();
//        ProviderService provider2 = ProviderService.builder()
//                .service(service)
//                .location(location2)
//                .build();
//
//        repository.save(provider1);
//        repository.save(provider2);
//
//        // Act
//        Page<ProviderService> result = repository.findByServiceNameAndLocationWithinRadiusNative(
//                serviceName, 12.9716, 77.5946, 50000, PageRequest.of(0, 10)); // 50km radius
//
//        // Assert
//        assertThat(result.getContent()).hasSize(1);
//        assertThat(result.getContent().get(0).getLocation()).isEqualTo(location1);
//    }
//}
