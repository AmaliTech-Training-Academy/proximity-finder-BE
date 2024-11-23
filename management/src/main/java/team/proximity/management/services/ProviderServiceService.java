// ProviderServiceService.java
package team.proximity.management.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
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
    private final BookingDayHoursValidator bookingDayHoursValidator;

    public ProviderServiceService(ProviderServiceRepository providerServiceRepository,
                                  ServicesRepository servicesRepository,
                                  S3Service s3Service,
                                  BookingDayHoursValidator bookingDayHoursValidator,
                                  ObjectMapper objectMapper) {
        this.providerServiceRepository = providerServiceRepository;
        this.bookingDayHoursValidator = bookingDayHoursValidator;
        this.objectMapper = objectMapper;
        // Initialize ProviderServiceMapper with dependencies
        this.preferenceMapper = new ProviderServiceMapper(s3Service, servicesRepository);
    }
    public ProviderService createOrUpdateProviderService(ProviderServiceRequest providerServiceRequest) throws JsonProcessingException {
        if (providerServiceRequest.getId() != null) {
            log.info("Updating existing providerService with id: {}", providerServiceRequest.getId());
            return updateProviderService(providerServiceRequest.getId(), providerServiceRequest);
        } else {
            log.info("Creating new providerService");
            return createProviderService(providerServiceRequest);
        }
    }

    public ProviderService createProviderService(ProviderServiceRequest providerServiceRequest) throws JsonProcessingException {
        List<BookingDayRequest> bookingDays = objectMapper.readValue(
                providerServiceRequest.getBookingDays(), new TypeReference<List<BookingDayRequest>>() {});
        log.info("Creating new preference with request: {}", providerServiceRequest);
        for (BookingDayRequest bookingDayRequest : bookingDays) {
            bookingDayHoursValidator.validate(bookingDayRequest);

        }
        log.info("Creating new preference with request: {}", providerServiceRequest);
        ProviderService preference = preferenceMapper.toEntity(providerServiceRequest, bookingDays);
        log.info("Creating new preference with request: {}", providerServiceRequest);
        preference.setCreatedAt(LocalDateTime.now());
        preference.setUpdatedAt(LocalDateTime.now());
        return providerServiceRepository.save(preference);
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
}