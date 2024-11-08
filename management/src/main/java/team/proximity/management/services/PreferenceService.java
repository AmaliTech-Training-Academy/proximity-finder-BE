package team.proximity.management.services;

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

    public PreferenceService(PreferenceRepository preferenceRepository) {
        this.preferenceRepository = preferenceRepository;
    }

    public Preference createPreference(Preference preference) {
        preference.setCreatedAt(LocalDateTime.now());
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    public Preference updatePreference(UUID id, Preference updatedPreference) {
        Optional<Preference> optionalPreference = preferenceRepository.findById(id);
        if (optionalPreference.isPresent()) {
            Preference preference = optionalPreference.get();
            preference.setPaymentPreference(updatedPreference.getPaymentPreference());
            preference.setLocation(updatedPreference.getLocation());
            preference.setSameLocation(updatedPreference.getSameLocation());
            preference.setSchedulingPolicy(updatedPreference.getSchedulingPolicy());
//            preference.setDayOfWeek(updatedPreference.getDayOfWeek());
//            preference.setFromTime(updatedPreference.getFromTime());
//            preference.setToTime(updatedPreference.getToTime());
            preference.setUpdatedAt(LocalDateTime.now());
            return preferenceRepository.save(preference);
        } else {
            throw new IllegalArgumentException("Preference with ID " + id + " not found.");
        }
    }

    public Optional<Preference> getPreferenceById(UUID id) {
        return preferenceRepository.findById(id);
    }

    public List<Preference> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    public void deletePreference(UUID id) {
        preferenceRepository.deleteById(id);
    }
}