// PreferenceService.java
package team.proximity.management.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.requests.PreferenceRequest;
import team.proximity.management.exceptions.PreferenceNotFoundException;
import team.proximity.management.mappers.PreferenceMapper;
import team.proximity.management.model.ProviderService;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.PreferenceRepository;
import team.proximity.management.validators.BookingDayHoursValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProviderServiceService {

    private final ObjectMapper objectMapper;
    private final PreferenceRepository preferenceRepository;
    private final PreferenceMapper preferenceMapper;
    private final BookingDayHoursValidator bookingDayHoursValidator;

    public ProviderServiceService(PreferenceRepository preferenceRepository, S3Service s3Service, BookingDayHoursValidator bookingDayHoursValidator, ObjectMapper objectMapper) {
        this.preferenceRepository = preferenceRepository;
        this.preferenceMapper = new PreferenceMapper(s3Service);
        this.bookingDayHoursValidator = bookingDayHoursValidator;
        this.objectMapper = objectMapper;
    }


    public ProviderService createPreference(PreferenceRequest preferenceRequest) throws JsonProcessingException {
        List<BookingDayRequest> bookingDays = objectMapper.readValue(
                preferenceRequest.getBookingDays(), new TypeReference<List<BookingDayRequest>>() {});
    log.info("Creating new preference with request: {}", preferenceRequest);
        for (BookingDayRequest bookingDayRequest : bookingDays) {
            bookingDayHoursValidator.validate(bookingDayRequest);

        }
        log.info("Creating new preference with request: {}", preferenceRequest);
        ProviderService preference = preferenceMapper.toEntity(preferenceRequest, bookingDays);
        log.info("Creating new preference with request: {}", preferenceRequest);
        preference.setCreatedAt(LocalDateTime.now());
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    public ProviderService updatePreference(UUID id, PreferenceRequest updatedPreferenceRequest) {
        ProviderService preference = preferenceRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException(id));
        preferenceMapper.updateEntity(updatedPreferenceRequest, preference);
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    public Optional<ProviderService> getPreferenceById(UUID id) {
        return preferenceRepository.findById(id)
                .or(() -> { throw new PreferenceNotFoundException(id); });
    }

    public List<ProviderService> getAllPreferences() {
        return preferenceRepository.findAll();
    }

    public void deletePreference(UUID id) {
        if (!preferenceRepository.existsById(id)) {
            throw new PreferenceNotFoundException(id);
        }
        preferenceRepository.deleteById(id);
    }
}