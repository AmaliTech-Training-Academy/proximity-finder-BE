package team.proximity.management.requests;


import lombok.Data;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Data
public class BookingDayRequest {
    private DayOfWeek dayOfWeek;
    private LocalTime startTime;
    private LocalTime endTime;
}
