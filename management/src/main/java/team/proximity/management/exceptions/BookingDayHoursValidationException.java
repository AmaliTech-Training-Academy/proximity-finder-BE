package team.proximity.management.exceptions;



import team.proximity.management.responses.ErrorResponse;

import java.util.List;

public class BookingDayHoursValidationException extends RuntimeException {
    private final List<ErrorResponse> errors;

    public BookingDayHoursValidationException(List<ErrorResponse> errors) {
        super("Business hours validation failed");
        this.errors = errors;
    }

    public List<ErrorResponse> getErrors() {
        return errors;
    }
}
