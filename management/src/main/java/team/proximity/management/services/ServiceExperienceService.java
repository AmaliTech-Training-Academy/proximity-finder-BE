package team.proximity.management.services;


import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.model.ServiceExperience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.ServiceExperienceRepository;
import team.proximity.management.requests.ServiceExperienceRequest;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ServiceExperienceService {

    private final ServiceExperienceRepository repository;
    private final S3Service s3Service;

    @Autowired
    public ServiceExperienceService(ServiceExperienceRepository repository, S3Service s3Service) {
        this.repository = repository;
        this.s3Service = s3Service;
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
            return s3Service.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    // Helper method to convert List<MultipartFile> to List<String> URLs
    private List<String> uploadImages(List<MultipartFile> files) {
        return files.stream()
                .map(this::uploadFileToS3)
                .collect(Collectors.toList());
    }

    // Method to create a new ServiceExperience
    public ServiceExperience createServiceExperience(ServiceExperienceRequest request) {
        ServiceExperience serviceExperience = new ServiceExperience();
        return getServiceExperience(request, serviceExperience);
    }

    private ServiceExperience getServiceExperience(ServiceExperienceRequest request, ServiceExperience serviceExperience) {
        serviceExperience.setProjectTitle(request.getProjectTitle());
        serviceExperience.setDescription(request.getDescription());

        if (request.getImages() != null && !request.getImages().isEmpty()) {
            List<String> imageUrls = uploadImages(request.getImages());
            serviceExperience.setImages(imageUrls);
        }

        return repository.save(serviceExperience);
    }

    // Method to update an existing ServiceExperience
    public Optional<ServiceExperience> updateServiceExperience(Long id, ServiceExperienceRequest request) {
        return repository.findById(id).map(existing -> {
            return getServiceExperience(request, existing);
        });
    }


    public void deleteServiceExperience(Long id) {
        repository.deleteById(id);
    }
}

