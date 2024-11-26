// ProviderServiceService.java
package team.proximity.management.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import team.proximity.management.exceptions.CustomJsonProcessingException;
import team.proximity.management.exceptions.ResourceNotFoundException;
import team.proximity.management.repositories.ServicesRepository;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.requests.ProviderServiceRequest;
import team.proximity.management.exceptions.ProviderServiceNotFoundException;
import team.proximity.management.mappers.ProviderServiceMapper;
import team.proximity.management.model.ProviderService;
import org.springframework.stereotype.Service;
import team.proximity.management.repositories.ProviderServiceRepository;
import team.proximity.management.validators.BookingDayHoursValidator;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Slf4j
@Service
public class ProviderServiceService {

    private final ObjectMapper objectMapper;
    private final ProviderServiceRepository providerServiceRepository;
    private final ProviderServiceMapper preferenceMapper;

    private static final String LOG_UPDATE_PROVIDER_SERVICE = "Updating existing providerService with id: {}";
    private static final String LOG_CREATE_PROVIDER_SERVICE = "Creating new providerService";
    private static final String LOG_CREATE_PREFERENCE = "Creating new preference with request: {}";
    private static final String LOG_UPDATE_PROVIDER_SERVICE_ID = "Updating providerService with id: {}";
    private static final String LOG_FETCH_PROVIDER_SERVICE_ID = "Fetching providerService with id: {}";
    private static final String LOG_FETCH_ALL_PROVIDER_SERVICES = "Fetching all providerServices";
    private static final String LOG_DELETE_PROVIDER_SERVICE_ID = "Deleting providerService with id: {}";
    private static final String LOG_FETCH_PROVIDER_SERVICES_USER_ID = "Fetching providerServices for userId: {}";

    public ProviderServiceService(ProviderServiceRepository providerServiceRepository,
                                  ServicesRepository servicesRepository,
                                  S3Service s3Service,
                                  BookingDayHoursValidator bookingDayHoursValidator,
                                  ObjectMapper objectMapper) {
        this.providerServiceRepository = providerServiceRepository;
        this.objectMapper = objectMapper;
        // Initialize ProviderServiceMapper with dependencies
        this.preferenceMapper = new ProviderServiceMapper(s3Service, servicesRepository);
    }
    public ProviderService createOrUpdateProviderService(ProviderServiceRequest providerServiceRequest) throws JsonProcessingException {
        if (providerServiceRequest.getId() != null) {
            log.info(LOG_UPDATE_PROVIDER_SERVICE, providerServiceRequest.getId());
            return updateProviderService(providerServiceRequest.getId(), providerServiceRequest);
        } else {
            log.info(LOG_CREATE_PROVIDER_SERVICE);
            return createProviderService(providerServiceRequest);
        }
    }

    public ProviderService createProviderService(ProviderServiceRequest providerServiceRequest) {
        log.info(LOG_CREATE_PREFERENCE, providerServiceRequest);

        List<BookingDayRequest> bookingDays = parseBookingDays(providerServiceRequest);

        bookingDays.forEach(BookingDayHoursValidator::validate);
        log.info(LOG_CREATE_PREFERENCE, providerServiceRequest);
        ProviderService providerService = preferenceMapper.toEntity(providerServiceRequest, bookingDays);
        log.info(LOG_CREATE_PREFERENCE, providerServiceRequest);
        providerService.setCreatedAt(LocalDateTime.now());
        providerService.setUpdatedAt(LocalDateTime.now());

        return providerServiceRepository.save(providerService);
    }
    private List<BookingDayRequest> parseBookingDays(ProviderServiceRequest providerServiceRequest) {
        try {
            return objectMapper.readValue(
                    providerServiceRequest.getBookingDays(),
                    new TypeReference<List<BookingDayRequest>>() {});
        } catch (JsonProcessingException e) {
            log.error("Failed to process bookingDays JSON: {}", providerServiceRequest.getBookingDays(), e);
            throw new CustomJsonProcessingException("Invalid JSON for booking days", e);
        }
    }

    public ProviderService updateProviderService(UUID id, ProviderServiceRequest updatedProviderServiceRequest) throws JsonProcessingException {
        log.info(LOG_UPDATE_PROVIDER_SERVICE_ID, id);
        List<BookingDayRequest> bookingDays = objectMapper.readValue(
                updatedProviderServiceRequest.getBookingDays(), new TypeReference<List<BookingDayRequest>>() {});
        ProviderService preference = providerServiceRepository.findById(id)
                .orElseThrow(() -> new ProviderServiceNotFoundException(id));
        preferenceMapper.updateEntity(updatedProviderServiceRequest, preference, bookingDays);
        preference.setUpdatedAt(LocalDateTime.now());
        return providerServiceRepository.save(preference);
    }

    public ProviderService getProviderServiceById(UUID id) {
        log.info(LOG_FETCH_PROVIDER_SERVICE_ID, id);
        return providerServiceRepository.findById(id)
                .orElseThrow(() -> new ProviderServiceNotFoundException(id));
    }

    public List<ProviderService> getAllProviderServices() {
        log.info(LOG_FETCH_ALL_PROVIDER_SERVICES);
        return providerServiceRepository.findAll();
    }

    public void deleteProviderService(UUID id) {
        log.info(LOG_DELETE_PROVIDER_SERVICE_ID, id);
        providerServiceRepository.deleteById(id);
    }
    public List<ProviderService> getProviderServicesByUserId(UUID userId) {
        log.info(LOG_FETCH_PROVIDER_SERVICES_USER_ID, userId);
        return providerServiceRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Provider Service not found"));
    }
}