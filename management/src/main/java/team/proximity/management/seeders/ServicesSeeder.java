package team.proximity.management.seeders;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Component;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ServicesRepository;

import java.util.Arrays;
import java.util.List;

@Component
public class ServicesSeeder {
    @Autowired
    private ServicesRepository servicesRepository;

    public void seedServices() {
        List<Services> services = Arrays.asList(
                Services.builder()
                        .name("Cleaning")
                        .description("Professional home and office cleaning services")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build(),

                Services.builder()
                        .name("Plumbing")
                        .description("Expert plumbing repairs and installations")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build(),

                Services.builder()
                        .name("Electrical")
                        .description("Comprehensive electrical services and repairs")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build(),

                Services.builder()
                        .name("Painting")
                        .description("Interior and exterior painting services")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build(),

                Services.builder()
                        .name("Landscaping")
                        .description("Professional garden and yard maintenance")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build(),

                Services.builder()
                        .name("Carpentry")
                        .description("Custom woodworking and repair services")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build(),

                Services.builder()
                        .name("HVAC")
                        .description("Heating, ventilation, and air conditioning services")
                        .image("https://gtp-group-three-lms.s3.us-east-2.amazonaws.com/1732791301039_house1.png")
                        .build()
        );

        // Save all services, handling potential duplicates
        services.forEach(service -> {
            try {
                servicesRepository.save(service);
            } catch (DataIntegrityViolationException e) {
                // Log or handle duplicate service names
                System.out.println("Service already exists: " + service.getName());
            }
        });
    }

//    @PostConstruct
//    public void init() {
//        seedServices();
//    }
}