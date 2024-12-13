package team.proximity.management.services;


import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.FileUploadException;
import team.proximity.management.exceptions.ResourceNotFoundException;

import team.proximity.management.model.ServiceExperience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.ProviderServiceRepository;
import team.proximity.management.repositories.ServiceExperienceRepository;
import team.proximity.management.requests.ServiceExperienceRequest;
import team.proximity.management.validators.upload.ImageValidationStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ServiceExperienceService {

    private final ServiceExperienceRepository repository;
    private final S3Service s3Service;
    private final ProviderServiceRepository providerServiceRepository;

    @Autowired
    public ServiceExperienceService(ServiceExperienceRepository repository, S3Service s3Service, ProviderServiceRepository providerServiceRepository) {
        this.repository = repository;
        this.s3Service = s3Service;
        this.providerServiceRepository = providerServiceRepository;
    }

    public List<ServiceExperience> getAllServiceExperiences() {
        return repository.findAll();
    }

    public Optional<ServiceExperience> getServiceExperienceById(Long id) {
        return repository.findById(id);
    }
    // Helper method to handle S3 file upload
    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Service.uploadFile(file, new ImageValidationStrategy()).get("url");
        }
        catch (IOException e) {
            log.error("Failed to upload file to S3", e);
            throw new FileUploadException("Failed to upload file to S3", e);
        }
    }

    private List<String> uploadImages(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadFileToS3)
                .collect(Collectors.toList());
    }

    public ServiceExperience createServiceExperience( ServiceExperienceRequest request) {
        log.info("Creating service experience: {}", request);
        Optional<team.proximity.management.model.ProviderService> providerServiceOpt = providerServiceRepository.findById(request.getProviderServiceId());
        if (providerServiceOpt.isEmpty()) {
            throw new ResourceNotFoundException("ProviderService not found");
        }
        ServiceExperience serviceExperience = new ServiceExperience();
        serviceExperience.setProviderService(providerServiceOpt.get());
        return getServiceExperience(request, serviceExperience);
    }

    private ServiceExperience getServiceExperience(ServiceExperienceRequest request, ServiceExperience serviceExperience) {
        serviceExperience.setProjectTitle(request.getProjectTitle());
        serviceExperience.setDescription(request.getDescription());

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            log.debug("Uploading images: {}", request.getImages());
            List<String> imageUrls = uploadImages(request.getImages());
            log.debug("Setting project title: {}", request.getProjectTitle());
            serviceExperience.setImages(imageUrls);
        }

        return repository.save(serviceExperience);
    }

    public Optional<ServiceExperience> updateServiceExperience(Long id, ServiceExperienceRequest request) {
        return repository.findById(id).map(existing -> {
            return getServiceExperience(request, existing);
        });
    }


    public void deleteServiceExperience(Long id) {
        repository.deleteById(id);
    }
}

