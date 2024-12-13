package team.proximity.management.mappers;


import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.springframework.web.multipart.MultipartFile;
import team.proximity.management.exceptions.FileUploadException;
import team.proximity.management.model.*;
import team.proximity.management.repositories.ServicesRepository;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.requests.ProviderServiceRequest;
import team.proximity.management.services.S3Service;
import team.proximity.management.utils.AuthenticationHelper;
import team.proximity.management.validators.upload.PDFValidationStrategy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
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

    public void updateEntity(ProviderServiceRequest providerServiceRequest, ProviderService preference, List<BookingDayRequest> bookingDays) {

        updatePreferenceFields(providerServiceRequest, preference, bookingDays);


        updateDocuments(preference, providerServiceRequest.getDocuments());
    }
    private void updateDocuments(ProviderService preference, List<MultipartFile> newDocuments) {
        List<Document> currentDocuments = preference.getDocuments();
        currentDocuments.removeIf(doc ->
                newDocuments.stream().noneMatch(file -> file.getOriginalFilename().equals(doc.getUrl()))
        );

        newDocuments.forEach(file -> {
            if (currentDocuments.stream().noneMatch(doc -> doc.getUrl().equals(file.getOriginalFilename()))) {
                currentDocuments.add(createDocument(file, preference));
            }
        });
    }

    private ProviderService buildPreferenceFromDTO(ProviderServiceRequest dto, List<BookingDayRequest> bookingDays) {
        Optional<Services> service = servicesRepository.findByName(dto.getServiceName());
        if (service.isEmpty()) {
            Services newService = Services.builder()
                    .name(dto.getServiceName())
                    .build();
            newService = servicesRepository.save(newService);
            service = Optional.of(newService);
        }
        // Create GeometryFactory
        GeometryFactory geometryFactory = new GeometryFactory();

        // Create Point from latitude and longitude
        Point point = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));

        return ProviderService.builder()
                .userEmail(AuthenticationHelper.getCurrentUserEmail())
                .service(service.get())
                .paymentPreference(dto.getPaymentPreference())
                .location(point)
                .placeName(dto.getPlaceName())
                .schedulingPolicy(dto.getSchedulingPolicy())
                .bookingDays(mapBookingDays(bookingDays))
                .build();
    }

    private void updatePreferenceFields(ProviderServiceRequest dto, ProviderService preference, List<BookingDayRequest> bookingDays) {
        preference.setPaymentPreference(dto.getPaymentPreference());
        // Create GeometryFactory
        GeometryFactory geometryFactory = new GeometryFactory();

        // Create Point from latitude and longitude
        Point point = geometryFactory.createPoint(new Coordinate(dto.getLongitude(), dto.getLatitude()));
        preference.setLocation(point);
        preference.setPlaceName(dto.getPlaceName());
        preference.setSchedulingPolicy(dto.getSchedulingPolicy());
        preference.setBookingDays(mapBookingDays(bookingDays));
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
        Map<String, String> fileDetails = uploadFileToS3(file);
        return Document.builder()
                .url(fileDetails.get("url"))
                .fileName(fileDetails.get("fileName"))
                .preference(preference)
                .build();
    }

    private Map<String, String> uploadFileToS3(MultipartFile file) {
        try {
            return  s3Service.uploadFile(file, new PDFValidationStrategy());
        } catch (IOException e) {
            throw new FileUploadException("Failed to upload file to S3", e);
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