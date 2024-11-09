// PreferenceService.java
package team.proximity.management.services;

import team.proximity.management.requests.PreferenceRequest;
import team.proximity.management.exceptions.PreferenceNotFoundException;
import team.proximity.management.mappers.PreferenceMapper;
import team.proximity.management.model.Preference;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.PreferenceRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final PreferenceMapper preferenceMapper;

    public PreferenceService(PreferenceRepository preferenceRepository, S3Service s3Service) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceMapper = new PreferenceMapper(s3Service);
    }


    public Preference createPreference(PreferenceRequest preferenceRequest) {
        Preference preference = preferenceMapper.toEntity(preferenceRequest);
        preference.setCreatedAt(LocalDateTime.now());
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    public Preference updatePreference(UUID id, PreferenceRequest updatedPreferenceRequest) {
        Preference preference = preferenceRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException(id));
        preferenceMapper.updateEntity(updatedPreferenceRequest, preference);
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    public Optional<Preference> getPreferenceById(UUID id) {
        return preferenceRepository.findById(id)
                .or(() -> { throw new PreferenceNotFoundException(id); });
    }

    public List<Preference> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    public void deletePreference(UUID id) {
        if (!preferenceRepository.existsById(id)) {
            throw new PreferenceNotFoundException(id);
        }
        preferenceRepository.deleteById(id);
    }
}