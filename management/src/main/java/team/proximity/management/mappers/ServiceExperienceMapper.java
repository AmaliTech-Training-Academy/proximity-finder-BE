package team.proximity.management.mappers;

import team.proximity.management.dtos.ServiceExperienceDTO;
import team.proximity.management.model.ServiceExperience;

import java.util.stream.Collectors;


public class ServiceExperienceMapper {

    public static ServiceExperienceDTO toDto(ServiceExperience serviceExperience) {
        if (serviceExperience == null) {
            return null;
        }
        return ServiceExperienceDTO.builder()
                .id(serviceExperience.getId())
                .projectTitle(serviceExperience.getProjectTitle())
                .description(serviceExperience.getDescription())
                .images(serviceExperience.getImages().stream().collect(Collectors.toList()))
                .build();
    }
}

