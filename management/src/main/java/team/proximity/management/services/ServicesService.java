package team.proximity.management.services;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.FileUploadException;
import team.proximity.management.exceptions.ResourceNotFoundException;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ServicesRepository;
import team.proximity.management.requests.ServiceRequest;
import team.proximity.management.requests.UpdateServiceRequest;
import team.proximity.management.responses.ApiResponseStatus;
import team.proximity.management.responses.ErrorResponse;
import team.proximity.management.utils.Helpers;
import team.proximity.management.validators.upload.ImageValidationStrategy;
import team.proximity.management.validators.upload.PDFValidationStrategy;

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


    public List<Services> getAllServices() {
        log.info("ServicesService: get all services execution started");
        return servicesRepository.findAll();
    }

    public Optional<Services> getServiceById(UUID id) {
        log.info("ServicesService: get service by id execution started");
        return servicesRepository.findById(id);
    }

    @Transactional
    public Services createService(ServiceRequest serviceRequest) {
        String imageUrl = uploadFileToS3(serviceRequest.getImage());
        log.info("Image uploaded to S3: {}", imageUrl);
        Services service = Services.builder()
                .name(serviceRequest.getName())
                .description(serviceRequest.getDescription())
                .image(imageUrl)
                .build();

        return servicesRepository.save(service);
    }
    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Service.uploadFile(file, new ImageValidationStrategy());
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to S3", e);
        }
    }

    @Transactional
    public Services updateService(UUID id, UpdateServiceRequest serviceRequest) {
        return servicesRepository.findById(id)
                .map(service -> {
                    if (serviceRequest.getName() != null && !serviceRequest.getName().isEmpty()) {
                        service.setName(serviceRequest.getName());
                    }
                    if (serviceRequest.getDescription() != null && !serviceRequest.getDescription().isEmpty()) {
                        service.setDescription(serviceRequest.getDescription());
                    }
                    if (serviceRequest.getImage() != null && !serviceRequest.getImage().isEmpty()) {
                        updateServiceImage(service, serviceRequest.getImage());
                    }

                    return servicesRepository.save(service);
                })
                .orElseThrow(() -> new ResourceNotFoundException("Service not found with id " + id));
    }
    private void updateServiceImage(Services service, MultipartFile image) {
        try {
            String imageUrl = s3Service.uploadFile(image, new ImageValidationStrategy());
            service.setImage(imageUrl);
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload service Image", e);
        }
    }
    public void deleteService(UUID id) {
        servicesRepository.deleteById(id);
    }
}