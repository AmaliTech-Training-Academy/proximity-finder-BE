package team.proximity.management.services;

import org.springframework.transaction.annotation.Transactional;
import team.proximity.management.requests.ServiceRequest;
import team.proximity.management.exceptions.ResourceNotFoundException;
import team.proximity.management.model.Services;

import org.springframework.stereotype.Service;
import team.proximity.management.repositories.ServicesRepository;


import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class ServicesService {

    private final ServicesRepository servicesRepository;
    private final S3Service s3Service;
    public ServicesService(ServicesRepository servicesRepository, S3Service s3Service) {
        this.servicesRepository = servicesRepository;
        this.s3Service = s3Service;

    }

    public List<Services> getAllServices() {
        return servicesRepository.findAll();
    }

    public Optional<Services> getServiceById(UUID id) {
        return servicesRepository.findById(id);
    }


    @Transactional
    public Services createService(ServiceRequest serviceRequest) throws IOException {
        String imageUrl = s3Service.uploadFile(serviceRequest.getImage());

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
