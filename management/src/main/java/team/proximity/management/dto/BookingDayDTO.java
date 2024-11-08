package team.proximity.management.dto;


import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class BookingDayDTO {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
