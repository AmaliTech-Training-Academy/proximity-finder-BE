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
        request.setStartTime(LocalTime.of(23, 0)); // Opening time
        request.setEndTime(LocalTime.of(17, 0));   // Closing time

        BookingDayHoursValidationException exception = assertThrows(BookingDayHoursValidationException.class,
                () -> BookingDayHoursValidator.validate(request));

        List<ErrorResponse> errors = exception.getErrors();

        // Validate both errors
        assertEquals(2, errors.size());
        assertEquals("Closing time must be after opening time", errors.get(0).getMessage());
        assertEquals("Business hours must be at least 5 minutes", errors.get(1).getMessage());
    }

    // Add more tests for other scenarios
}