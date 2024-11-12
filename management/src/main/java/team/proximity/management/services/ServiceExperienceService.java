package team.proximity.management.services;


import team.proximity.management.model.ServiceExperience;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.ServiceExperienceRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ServiceExperienceService {

    private final ServiceExperienceRepository repository;

    @Autowired
    public ServiceExperienceService(ServiceExperienceRepository repository) {
        this.repository = repository;
    }

    public List<ServiceExperience> getAllServiceExperiences() {
        return repository.findAll();
    }

    public Optional<ServiceExperience> getServiceExperienceById(Long id) {
        return repository.findById(id);
    }

    public ServiceExperience createServiceExperience(ServiceExperience serviceExperience) {
        return repository.save(serviceExperience);
    }

    public ServiceExperience updateServiceExperience(Long id, ServiceExperience serviceExperience) {
        serviceExperience.setId(id);
        return repository.save(serviceExperience);
    }

    public void deleteServiceExperience(Long id) {
        repository.deleteById(id);
    }
}

