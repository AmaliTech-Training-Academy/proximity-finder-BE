package team.proximity.management.mappers;



import team.proximity.management.dtos.ProviderServiceDTO;
import team.proximity.management.model.ProviderService;

import java.util.List;
import java.util.stream.Collectors;

public class ProviderServiceResponseMapper {

    public static ProviderServiceDTO toDto(ProviderService providerService) {
        return ProviderServiceDTO.builder()
                .id(providerService.getId())
                .userEmail(providerService.getUserEmail())
                .paymentPreference(providerService.getPaymentPreference())
//                .location(providerService.getLocation())
                .service(ServiceMapper.toDto(providerService.getService()))
                .placeName(providerService.getPlaceName())
                .serviceExperience(ServiceExperienceMapper.toDto(providerService.getServiceExperience()))
                .schedulingPolicy(providerService.getSchedulingPolicy())
                .bookingDays(BookingDayMapper.toDtoList(providerService.getBookingDays()))
                .documents(DocumentMapper.toDtoList(providerService.getDocuments()))
                .createdAt(providerService.getCreatedAt())
                .updatedAt(providerService.getUpdatedAt())
                .build();
    }

    public static List<ProviderServiceDTO> toDtoList(List<ProviderService> providerServices) {
        return providerServices.stream()
                .map(ProviderServiceResponseMapper::toDto)
                .collect(Collectors.toList());
    }
}
