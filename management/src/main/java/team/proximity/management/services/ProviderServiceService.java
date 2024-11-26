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


    public ProviderServiceService(ProviderServiceRepository providerServiceRepository,
                                  ServicesRepository servicesRepository,
                                  S3Service s3Service,
                                  ObjectMapper objectMapper) {
        this.providerServiceRepository = providerServiceRepository;
        this.objectMapper = objectMapper;
        this.preferenceMapper = new ProviderServiceMapper(s3Service, servicesRepository);
    }
    public ProviderService createOrUpdateProviderService(ProviderServiceRequest providerServiceRequest) {
        if (providerServiceRequest.getId() != null) {
            log.info("Updating existing providerService with id: {}", providerServiceRequest.getId());
            return updateProviderService(providerServiceRequest.getId(), providerServiceRequest);
        } else {
            log.info("Creating new providerService");
            return createProviderService(providerServiceRequest);
        }
    }

    public ProviderService createProviderService(ProviderServiceRequest providerServiceRequest) {
        log.info("Creating new provider service with request: {}", providerServiceRequest);

        List<BookingDayRequest> bookingDays = parseBookingDays(providerServiceRequest);

        bookingDays.forEach(BookingDayHoursValidator::validate);

        ProviderService providerService = preferenceMapper.toEntity(providerServiceRequest, bookingDays);
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


    public ProviderService updateProviderService(UUID id, ProviderServiceRequest updatedProviderServiceRequest) {
        ProviderService preference = providerServiceRepository.findById(id)
                .orElseThrow(() -> new ProviderServiceNotFoundException(id));
        preferenceMapper.updateEntity(updatedProviderServiceRequest, preference);
        preference.setUpdatedAt(LocalDateTime.now());
        return providerServiceRepository.save(preference);
    }

    public ProviderService getProviderServiceById(UUID id) {
        return providerServiceRepository.findById(id)
                .orElseThrow(() -> new ProviderServiceNotFoundException(id));
    }

    public List<ProviderService> getAllProviderServices() {
        return providerServiceRepository.findAll();
    }

    public void deleteProviderService(UUID id) {
        providerServiceRepository.deleteById(id);
    }
    public List<ProviderService> getProviderServicesByUserId(UUID userId) {
        return providerServiceRepository.findByUserId(userId).orElseThrow(() -> new ResourceNotFoundException("Provider Service not found"));
    }
}