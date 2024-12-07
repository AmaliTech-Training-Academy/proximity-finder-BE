package team.proximity.management.mappers;

import team.proximity.management.dtos.ServiceDTO;
import team.proximity.management.model.Services;

class ServiceMapper {
    public static ServiceDTO toDto(Services service) {
        return ServiceDTO.builder()
                .id(service.getId())
                .name(service.getName())
                .description(service.getDescription())
                .image(service.getImage())
                .build();
    }
}