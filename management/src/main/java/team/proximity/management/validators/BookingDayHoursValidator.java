package team.proximity.management.validators;

import org.springframework.stereotype.Component;
import team.proximity.management.exceptions.BookingDayHoursValidationException;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.responses.ErrorResponse;

import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Component
 public final class BookingDayHoursValidator {
    private static final LocalTime MIN_TIME = LocalTime.of(0, 0);
    private static final LocalTime MAX_TIME = LocalTime.of(23, 59);
    private static final String VALIDATION_ERROR = "Validation Error";
    private static final String OPENING_TIME_ERROR = "Opening time must be between 00:00 and 23:59";
    private static final String CLOSING_TIME_ERROR = "Closing time must be between 00:00 and 23:59";
    private static final String TIME_ORDER_ERROR = "Closing time must be after opening time";
    private static final String MIN_HOURS_ERROR = "Business hours must be at least 5 minutes";

    private BookingDayHoursValidator() {
    }
    public static void validate(BookingDayRequest dto) {
        List<ErrorResponse> errors = new ArrayList<>();
        validateTimes(dto.getStartTime(), dto.getEndTime(), errors);
        validateTimeRange(dto.getStartTime(), dto.getEndTime(), errors);
        if (!errors.isEmpty()) {
            throw new BookingDayHoursValidationException(errors);
        }
    }

    private static void validateTimes(LocalTime openingTime, LocalTime closingTime, List<ErrorResponse> errors) {
        // Check if times are within valid range (00:00 to 23:59)

        if (openingTime != null && (openingTime.isBefore(MIN_TIME) || openingTime.isAfter(MAX_TIME))) {
            errors.add(new ErrorResponse(VALIDATION_ERROR, OPENING_TIME_ERROR));
        }

        if (closingTime != null && (closingTime.isBefore(MIN_TIME) || closingTime.isAfter(MAX_TIME))) {
            errors.add(new ErrorResponse(VALIDATION_ERROR, CLOSING_TIME_ERROR));
        }
    }

    private static void validateTimeRange(LocalTime openingTime, LocalTime closingTime, List<ErrorResponse> errors) {
        if (openingTime != null && closingTime != null) {
            // Check if closing time is after opening time
            if (!closingTime.isAfter(openingTime)) {
                errors.add(new ErrorResponse(VALIDATION_ERROR, TIME_ORDER_ERROR));
            }

            if (openingTime.plusMinutes(5).isAfter(closingTime)) {
                errors.add(new ErrorResponse(VALIDATION_ERROR, MIN_HOURS_ERROR));
            }
        }
    }
}