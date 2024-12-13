package team.proximity.management.services;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.hibernate.sql.Update;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.requests.ServiceRequest;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ServicesRepository;
import team.proximity.management.requests.UpdateServiceRequest;
import team.proximity.management.validators.upload.ImageValidationStrategy;

import java.io.IOException;
import java.util.*;

class ServicesServiceTest {

    @InjectMocks
    private ServicesService servicesService;

    @Mock
    private ServicesRepository servicesRepository;

    @Mock
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllServicesTest() {
        // Arrange
        Services service1 = new Services(UUID.randomUUID(), "Service 1", "Description 1",  "image1.jpg");
        Services service2 = new Services(UUID.randomUUID(), "Service 2", "Description 2",  "image2.jpg");
        when(servicesRepository.findAll()).thenReturn(Arrays.asList(service1, service2));

        // Act
        List<Services> services = servicesService.getAllServices();

        // Assert
        assertThat(services).hasSize(2);
        verify(servicesRepository, times(1)).findAll();
    }

    @Test
    void getServiceByIdTest() {
        // Arrange
        UUID serviceId = UUID.randomUUID();
        Services service = new Services(serviceId, "Service 1", "Description 1", "image1.jpg");
        when(servicesRepository.findById(serviceId)).thenReturn(Optional.of(service));

        // Act
        Services foundService = servicesService.getServiceById(serviceId);

        // Assert

        assertThat(foundService.getName()).isEqualTo("Service 1");
        verify(servicesRepository, times(1)).findById(serviceId);
    }

    @Test
    void createServiceTest() throws IOException {
        // Arrange
        MultipartFile imageFile = mock(MultipartFile.class);
        ImageValidationStrategy imageValidationStrategy = new ImageValidationStrategy(); // Concrete instance

        when(imageFile.getOriginalFilename()).thenReturn("image.jpg");
        when(s3Service.uploadFile(eq(imageFile), any(ImageValidationStrategy.class)))
                .thenReturn(Map.of("url", "s3://bucket/image.jpg"));

        ServiceRequest serviceRequest = new ServiceRequest("New Service", "New Description", imageFile);
        Services service = new Services(UUID.randomUUID(), "New Service", "New Description", "s3://bucket/image.jpg");
        when(servicesRepository.save(any(Services.class))).thenReturn(service);

        // Act
        Services createdService = servicesService.createService(serviceRequest);

        // Assert
        assertThat(createdService.getImage()).isEqualTo("s3://bucket/image.jpg");
        assertThat(createdService.getName()).isEqualTo("New Service");
        verify(s3Service, times(1)).uploadFile(eq(imageFile), any(ImageValidationStrategy.class));
        verify(servicesRepository, times(1)).save(any(Services.class));
    }

    @Test
    void updateServiceTest() throws IOException {
        // Arrange
        UUID serviceId = UUID.randomUUID();
        MultipartFile newImage = mock(MultipartFile.class);
        when(newImage.getOriginalFilename()).thenReturn("newImage.jpg");

        // Use the same instance for the ImageValidationStrategy
        ImageValidationStrategy imageValidationStrategy = new ImageValidationStrategy();
        when(s3Service.uploadFile(newImage, imageValidationStrategy))
                .thenReturn(Map.of("url", "s3://bucket/newImage.jpg"));

        UpdateServiceRequest serviceRequest = new UpdateServiceRequest("Updated Service", "Updated Description", newImage);
        Services existingService = new Services(serviceId, "Old Service", "Old Description", "oldImage.jpg");

        when(servicesRepository.findById(serviceId)).thenReturn(Optional.of(existingService));
        Services updatedService = new Services(serviceId, "Updated Service", "Updated Description", "s3://bucket/newImage.jpg");
        when(servicesRepository.save(any(Services.class))).thenReturn(updatedService);

        // Act
        Services updatedServiceResult = servicesService.updateService(serviceId, serviceRequest);

        // Assert
        assertThat(updatedServiceResult.getImage()).isEqualTo("s3://bucket/newImage.jpg");
        assertThat(updatedServiceResult.getName()).isEqualTo("Updated Service");

        // Use ArgumentMatchers.any() for both arguments
        verify(s3Service, times(1)).uploadFile(ArgumentMatchers.any(MultipartFile.class), ArgumentMatchers.any(ImageValidationStrategy.class));
        verify(servicesRepository, times(1)).save(existingService);
    }




    @Test
    void updateServiceTest_ServiceNotFound() {
        // Arrange
        UUID serviceId = UUID.randomUUID();
       UpdateServiceRequest serviceRequest = new UpdateServiceRequest("Updated Service", "Updated Description",  null);

        when(servicesRepository.findById(serviceId)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            servicesService.updateService(serviceId, serviceRequest);
        });

        assertThat(exception.getMessage()).isEqualTo("Service not found with id " + serviceId);
        verify(servicesRepository, times(1)).findById(serviceId);
    }

    @Test
    void deleteServiceTest() {
        // Arrange
        UUID serviceId = UUID.randomUUID();
        doNothing().when(servicesRepository).deleteById(serviceId);

        // Act
        servicesService.deleteService(serviceId);

        // Assert
        verify(servicesRepository, times(1)).deleteById(serviceId);
    }
}