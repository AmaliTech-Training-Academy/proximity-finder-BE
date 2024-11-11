package team.proximity.management.validators;

import org.springframework.stereotype.Component;
import team.proximity.management.exceptions.BookingDayHoursValidationException;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.responses.ErrorResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class BookingDayHoursValidator {

    public void validate(BookingDayRequest dto) {
        List<ErrorResponse> errors = new ArrayList<>();

            validateTimes(dto.getStartTime(), dto.getEndTime(), errors);
            validateTimeRange(dto.getStartTime(), dto.getEndTime(), errors);


        if (!errors.isEmpty()) {
            throw new BookingDayHoursValidationException(errors);
        }
    }

    private void validateTimes(LocalTime openingTime, LocalTime closingTime, List<ErrorResponse> errors) {
        // Check if times are within valid range (00:00 to 23:59)
        LocalTime minTime = LocalTime.of(0, 0);
        LocalTime maxTime = LocalTime.of(23, 59);

        if (openingTime != null && (openingTime.isBefore(minTime) || openingTime.isAfter(maxTime))) {
            errors.add(new ErrorResponse("Validation Error","Opening time must be between 00:00 and 23:59"));
        }

        if (closingTime != null && (closingTime.isBefore(minTime) || closingTime.isAfter(maxTime))) {
            errors.add(new ErrorResponse("Validation Error","Closing time must be between 00:00 and 23:59"));
        }
    }

    private void validateTimeRange(LocalTime openingTime, LocalTime closingTime, List<ErrorResponse> errors) {
        if (openingTime != null && closingTime != null) {
            // Check if closing time is after opening time
            if (!closingTime.isAfter(openingTime)) {
                errors.add(new ErrorResponse("Validation Error","Closing time must be after opening time"));
            }

            if (openingTime.plusMinutes(5).isAfter(closingTime)) {
                errors.add(new ErrorResponse("Validation Error","Business hours must be at least 5 minutes"));
            }
        }
    }
}