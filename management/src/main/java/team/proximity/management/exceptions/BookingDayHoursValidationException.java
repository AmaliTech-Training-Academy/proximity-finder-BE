package team.proximity.management.exceptions;



import lombok.Getter;
import team.proximity.management.responses.ErrorResponse;

import java.util.List;

@Getter
public class BookingDayHoursValidationException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public BookingDayHoursValidationException(List<ErrorResponse> errors) {
        super("Business hours validation failed");
        this.errors = errors;
    }

}
