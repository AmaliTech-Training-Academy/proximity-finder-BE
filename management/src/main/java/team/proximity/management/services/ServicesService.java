package team.proximity.management.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import team.proximity.management.exceptions.ResourceNotFoundException;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ServicesRepository;
import team.proximity.management.requests.ServiceRequest;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;
import team.proximity.management.utils.Helpers;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ServicesService {
    private final ServicesRepository servicesRepository;
    private final S3Service s3Service;

    public ServicesService(ServicesRepository servicesRepository, S3Service s3Service) {
        this.servicesRepository = servicesRepository;
        this.s3Service = s3Service;
    }

    @Cacheable(value = "services")
    public List<Services> getAllServices() {
        log.info("ServicesService: get all services execution started");
        return servicesRepository.findAll();
    }

    public Optional<Services> getServiceById(UUID id) {
        log.info("ServicesService: get service by id execution started");
        return servicesRepository.findById(id);
    }

    @Transactional
    public Services createService(ServiceRequest serviceRequest) throws IOException {
        log.info("Creating service: {}", Helpers.jsonAsString(serviceRequest));
        String imageUrl = s3Service.uploadFile(serviceRequest.getImage());
        log.info("Image uploaded to S3: {}", imageUrl);
        Services service = Services.builder()
                .name(serviceRequest.getName())
                .description(serviceRequest.getDescription())
                .category(serviceRequest.getCategory())
                .image(imageUrl)
                .build();

        return servicesRepository.save(service);
    }

    @Transactional
    public Services updateService(UUID id, ServiceRequest serviceRequest) {
        return servicesRepository.findById(id)
                .map(service -> {
                    service.setName(serviceRequest.getName());
                    service.setDescription(serviceRequest.getDescription());
                    service.setCategory(serviceRequest.getCategory());

                    if (serviceRequest.getImage() != null && !serviceRequest.getImage().isEmpty()) {
                        try {
                            String imageUrl = s3Service.uploadFile(serviceRequest.getImage());
                            service.setImage(imageUrl);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return servicesRepository.save(service);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id));
    }

    public void deleteService(UUID id) {
        servicesRepository.deleteById(id);
    }
}