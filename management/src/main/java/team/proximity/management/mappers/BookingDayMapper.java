package team.proximity.management.mappers;

import team.proximity.management.dtos.BookingDayDTO;
import team.proximity.management.model.BookingDay;

import java.util.List;
import java.util.stream.Collectors;

public class BookingDayMapper {
    public static BookingDayDTO toDto(BookingDay bookingDay) {
        return BookingDayDTO.builder()
                .id(bookingDay.getId())
                .day(bookingDay.getDayOfWeek())
                .startTime(bookingDay.getFromTime())
                .endTime(bookingDay.getToTime())
                .build();
    }

    public static List<BookingDayDTO> toDtoList(List<BookingDay> bookingDays) {
        return bookingDays.stream()
                .map(BookingDayMapper::toDto)
                .collect(Collectors.toList());
    }
}

