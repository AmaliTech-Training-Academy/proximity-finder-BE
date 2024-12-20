package team.proximity.request_management.request_management.scheduling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EventMapperTest {

    private final EventMapper eventMapper = new EventMapper();

    @Test
    void shouldMapToEvent() {
        EventRequest request = new EventRequest(
                "Meeting",
                "2024-12-21",
                "10:00",
                "2024-12-21",
                "12:00",
                "Discuss project updates"
        );

        Event event = eventMapper.mapToEvent(request);

        assertEquals("Meeting", event.getTitle());
        assertEquals("2024-12-21", event.getStartDate());
        assertEquals("10:00", event.getStartTime());
        assertEquals("2024-12-21", event.getEndDate());
        assertEquals("12:00", event.getEndTime());
        assertEquals("Discuss project updates", event.getDescription());
    }

    @Test
    void shouldMapToEventResponse() {
        Event event = Event.builder()
                .eventId(1L)
                .title("Workshop")
                .startDate("2024-12-22")
                .startTime("09:00")
                .endDate("2024-12-22")
                .endTime("11:00")
                .description("Technical workshop")
                .createdBy("test@example.com")
                .build();

        EventResponse response = eventMapper.toEventResponse(event);

        assertEquals(1L, response.eventId());
        assertEquals("Workshop", response.title());
        assertEquals("2024-12-22", response.startDate());
        assertEquals("09:00", response.startTime());
        assertEquals("2024-12-22", response.endDate());
        assertEquals("11:00", response.endTime());
        assertEquals("Technical workshop", response.description());
        assertEquals("test@example.com", response.createdBy());
    }
}
