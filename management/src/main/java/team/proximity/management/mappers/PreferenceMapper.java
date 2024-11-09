package team.proximity.management.mappers;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.requests.PreferenceRequest;
import team.proximity.management.model.BookingDay;
import team.proximity.management.model.Document;
import team.proximity.management.model.Preference;
import team.proximity.management.services.S3Service;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class PreferenceMapper {
    private final S3Service s3Service;

    public PreferenceMapper(S3Service s3Service) {
        this.s3Service = s3Service;
    }

    public Preference toEntity(PreferenceRequest preferenceRequest) {
        // Create the Preference entity
        Preference preference = buildPreferenceFromDTO(preferenceRequest);

        // Handle documents
        List<Document> documents = createDocuments(preferenceRequest, preference);
        preference.setDocuments(documents);

        return preference;
    }

    public void updateEntity(PreferenceRequest preferenceRequest, Preference preference) {
        // Update basic fields
        updatePreferenceFields(preferenceRequest, preference);

        // Update documents
        List<Document> documents = createDocuments(preferenceRequest, preference);
        preference.setDocuments(documents);
    }

    private Preference buildPreferenceFromDTO(PreferenceRequest dto) {
        return Preference.builder()
                .userId(dto.getUserId())
                .serviceId(dto.getServiceId())
                .paymentPreference(dto.getPaymentPreference())
                .location(dto.getLocation())
                .sameLocation(dto.getSameLocation())
                .schedulingPolicy(dto.getSchedulingPolicy())
                .bookingDays(mapBookingDays(dto.getBookingDays()))
                .build();
    }

    private void updatePreferenceFields(PreferenceRequest dto, Preference preference) {
        preference.setPaymentPreference(dto.getPaymentPreference());
        preference.setLocation(dto.getLocation());
        preference.setSameLocation(dto.getSameLocation());
        preference.setSchedulingPolicy(dto.getSchedulingPolicy());
        preference.setBookingDays(mapBookingDays(dto.getBookingDays()));
    }

    private List<BookingDay> mapBookingDays(List<BookingDayRequest> dtos) {
        return dtos.stream()
                .map(this::mapToBookingDay)
                .collect(Collectors.toList());
    }

    private List<Document> createDocuments(PreferenceRequest dto, Preference preference) {
        return dto.getDocuments().stream()
                .map(file -> createDocument(file, preference))
                .collect(Collectors.toList());
    }

    private Document createDocument(MultipartFile file, Preference preference) {
        String imageUrl = uploadFileToS3(file);
        return Document.builder()
                .url(imageUrl)
                .preference(preference)
                .build();
    }

    private String uploadFileToS3(MultipartFile file) {
        try {
            return s3Service.uploadFile(file);
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to S3", e);
        }
    }

    private BookingDay mapToBookingDay(BookingDayRequest dto) {
        return BookingDay.builder()
                .dayOfWeek(dto.getDayOfWeek())
                .fromTime(dto.getStartTime())
                .toTime(dto.getEndTime())
                .build();
    }
}