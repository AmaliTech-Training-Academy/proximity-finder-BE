package team.proximity.management.mappers;

import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.ResourceNotFoundException;
import team.proximity.management.model.Services;
import team.proximity.management.repositories.ServicesRepository;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.requests.ProviderServiceRequest;
import team.proximity.management.model.BookingDay;
import team.proximity.management.model.Document;
import team.proximity.management.model.ProviderService;
import team.proximity.management.services.S3Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ProviderServiceMapper {
    private final S3Service s3Service;
    private final ServicesRepository servicesRepository;
    public ProviderServiceMapper(S3Service s3Service, ServicesRepository servicesRepository) {
        this.s3Service = s3Service;
        this.servicesRepository = servicesRepository;
    }

    public ProviderService toEntity(ProviderServiceRequest providerServiceRequest, List<BookingDayRequest> bookingDays) {
        // Create the Preference entity
        ProviderService preference = buildPreferenceFromDTO(providerServiceRequest, bookingDays);

        // Handle documents
        List<Document> documents = createDocuments(providerServiceRequest, preference);
        preference.setDocuments(documents);

        return preference;
    }

    public void updateEntity(ProviderServiceRequest providerServiceRequest, ProviderService preference) {
        // Update basic fields
        updatePreferenceFields(providerServiceRequest, preference);

        // Update documents
        List<Document> documents = createDocuments(providerServiceRequest, preference);
        preference.setDocuments(documents);
    }

    private ProviderService buildPreferenceFromDTO(ProviderServiceRequest dto, List<BookingDayRequest> bookingDays) {
        Optional<Services> service = servicesRepository.findById(dto.getServiceId());
        if (service.isEmpty()) {
            throw new ResourceNotFoundException("Service not found");
        }
        return ProviderService.builder()
                .userId(dto.getUserId())
                .service(service.get())
                .paymentPreference(dto.getPaymentPreference())
                .location(dto.getLocation())
                .sameLocation(dto.getSameLocation())
                .schedulingPolicy(dto.getSchedulingPolicy())
                .bookingDays(mapBookingDays(bookingDays))
                .build();
    }

    private void updatePreferenceFields(ProviderServiceRequest dto, ProviderService preference) {
        preference.setPaymentPreference(dto.getPaymentPreference());
        preference.setLocation(dto.getLocation());
        preference.setSameLocation(dto.getSameLocation());
        preference.setSchedulingPolicy(dto.getSchedulingPolicy());
//        preference.setBookingDays(mapBookingDays(dto.getBookingDays()));
    }

    private List<BookingDay> mapBookingDays(List<BookingDayRequest> dtos) {
        return dtos.stream()
                .map(this::mapToBookingDay)
                .collect(Collectors.toList());
    }

    private List<Document> createDocuments(ProviderServiceRequest dto, ProviderService preference) {
        return dto.getDocuments().stream()
                .map(file -> createDocument(file, preference))
                .collect(Collectors.toList());
    }

    private Document createDocument(MultipartFile file, ProviderService preference) {
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