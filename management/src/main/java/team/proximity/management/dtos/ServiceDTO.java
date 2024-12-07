package team.proximity.management.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceDTO {
    private UUID id;
    private String name;
    private String description;
    private String image;

}
