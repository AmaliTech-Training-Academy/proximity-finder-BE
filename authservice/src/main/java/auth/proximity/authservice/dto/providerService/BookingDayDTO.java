package auth.proximity.authservice.dto.providerService;



import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookingDayDTO {
    private UUID id;
    private DayOfWeek day;
    private LocalTime startTime;
    private LocalTime endTime;
}