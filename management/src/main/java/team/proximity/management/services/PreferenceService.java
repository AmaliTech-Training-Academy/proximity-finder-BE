package team.proximity.management.services;

import team.proximity.management.dto.BookingDayDTO;
import team.proximity.management.dto.PreferenceDTO;
import team.proximity.management.exceptions.PreferenceNotFoundException;
import team.proximity.management.model.BookingDay;
import team.proximity.management.model.Document;
import team.proximity.management.model.Preference;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.PreferenceRepository;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PreferenceService {
    private final PreferenceRepository preferenceRepository;
    private final S3Service s3Service;

    public PreferenceService(PreferenceRepository preferenceRepository, S3Service s3Service) {
        this.preferenceRepository = preferenceRepository;
        this.s3Service = s3Service;
    }

    public Preference createPreference(PreferenceDTO preferenceDTO) {
        Preference preference = new Preference();
        preference.setUserId(preferenceDTO.getUserId());
        preference.setServiceId(preferenceDTO.getServiceId());
        buildPreference(preferenceDTO, preference);

        preference.setCreatedAt(LocalDateTime.now());
        preference.setUpdatedAt(LocalDateTime.now());
        return preferenceRepository.save(preference);
    }

    private void buildPreference(PreferenceDTO preferenceDTO, Preference preference) {
        preference.setPaymentPreference(preferenceDTO.getPaymentPreference());
        preference.setLocation(preferenceDTO.getLocation());
        preference.setSameLocation(preferenceDTO.getSameLocation());
        preference.setSchedulingPolicy(preferenceDTO.getSchedulingPolicy());

        // Map BookingDayDTOs to BookingDay entities
        List<BookingDay> bookingDays = preferenceDTO.getBookingDays().stream()
                .map(this::mapToBookingDay)
                .collect(Collectors.toList());
        preference.setBookingDays(bookingDays);

        // Upload documents to S3 and set URLs
        List<Document> documents = preferenceDTO.getDocuments().stream()
                .map(file -> {
                    String imageUrl= null;
                    try {
                        imageUrl = s3Service.uploadFile(file);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    Document document = new Document();
//                    document.setFileName(fileName);
                    document.setUrl(imageUrl);
                    document.setPreference(preference);
                    return document;
                })
                .collect(Collectors.toList());

        preference.setDocuments(documents);
    }



    private BookingDay mapToBookingDay(BookingDayDTO dto) {
        BookingDay bookingDay = new BookingDay();
        bookingDay.setDayOfWeek(dto.getDayOfWeek());
        bookingDay.setFromTime(dto.getStartTime());
        bookingDay.setToTime(dto.getEndTime());
        return bookingDay;
    }

    public Preference updatePreference(UUID id, PreferenceDTO updatedPreferenceDTO) {
        Preference preference = preferenceRepository.findById(id)
                .orElseThrow(() -> new PreferenceNotFoundException(id));
        buildPreference(updatedPreferenceDTO, preference);

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