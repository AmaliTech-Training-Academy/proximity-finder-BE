package team.proximity.management.services;


import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ServicesRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@DataJpaTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServicesRepositoryTest {

    @Autowired
    private ServicesRepository servicesRepository;

    private static UUID serviceId;

    @Test
    @Order(1)
    @Rollback(value = false)
    void saveServiceTest() {
        Services service = new Services();
        service.setName("Test Service");
        service.setDescription("This is a test service description.");
        service.setImage("test_image.jpg");

        Services savedService = servicesRepository.save(service);
        serviceId = savedService.getId();

        Assertions.assertThat(savedService.getId()).isNotNull();
        Assertions.assertThat(savedService.getName()).isEqualTo("Test Service");
    }

    @Test
    @Order(2)
    void getServiceByIdTest() {

        Optional<Services> service = servicesRepository.findById(serviceId);


        Assertions.assertThat(service).isPresent();
        Assertions.assertThat(service.get().getId()).isEqualTo(serviceId);
    }

    @Test
    @Order(3)
    void getAllServicesTest() {

        List<Services> services = servicesRepository.findAll();

        Assertions.assertThat(services).isNotEmpty();
    }

    @Test
    @Order(4)
    @Rollback(value = false)
    void updateServiceTest() {

        Services service = servicesRepository.findById(serviceId).get();
        service.setName("Updated Test Service");

        Services updatedService = servicesRepository.save(service);


        Assertions.assertThat(updatedService.getName()).isEqualTo("Updated Test Service");
    }

    @Test
    @Order(5)
    @Rollback(value = false)
    void deleteServiceTest() {

        servicesRepository.deleteById(serviceId);
        Optional<Services> service = servicesRepository.findById(serviceId);

        Assertions.assertThat(service).isEmpty();
    }
}

