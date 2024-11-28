package team.proximity.management.validators;


import org.junit.jupiter.api.Test;
import team.proximity.management.exceptions.BookingDayHoursValidationException;
import team.proximity.management.requests.BookingDayRequest;
import team.proximity.management.responses.ErrorResponse;

import java.time.LocalTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BookingDayHoursValidatorTest {



    @Test
    void testValidTimes() {
        BookingDayRequest request = new BookingDayRequest();
        request.setStartTime(LocalTime.of(9, 0));
        request.setEndTime(LocalTime.of(17, 0));

        assertDoesNotThrow(() -> BookingDayHoursValidator.validate(request));
    }

    @Test
    void testInvalidOpeningTime() {
        BookingDayRequest request = new BookingDayRequest();
        request.setStartTime(LocalTime.of(24, 0));
        request.setEndTime(LocalTime.of(17, 0));

        BookingDayHoursValidationException exception = assertThrows(BookingDayHoursValidationException.class, () -> BookingDayHoursValidator.validate(request));
        List<ErrorResponse> errors = exception.getErrors();
        assertEquals(1, errors.size());
        assertEquals("Opening time must be between 00:00 and 23:59. Provided: 24:00", errors.get(0).getMessage());
    }

    // Add more tests for other scenarios
}