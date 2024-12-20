package team.proximity.request_management.request_management.scheduling;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class EventRepositoryTest {

    @Autowired
    private EventRepository eventRepository;

    @Test
    void shouldFindEventsByCreatedBy() {
        Event event = Event.builder()
                .title("Meeting")
                .startDate("2024-12-26")
                .startTime("10:00")
                .endDate("2024-12-26")
                .endTime("11:00")
                .createdBy("test@example.com")
                .build();

        eventRepository.save(event);

        List<Event> events = eventRepository.findByCreatedBy("test@example.com");

        assertEquals(1, events.size());
        assertEquals("Meeting", events.get(0).getTitle());
    }

    @Test
    void shouldFindEventsOnDate() {
        Event event = Event.builder()
                .title("Workshop")
                .startDate("2024-12-27")
                .endDate("2024-12-28")
                .createdBy("test@example.com")
                .build();

        eventRepository.save(event);

        List<Event> events = eventRepository.findEventsOnDate("2024-12-27", "test@example.com");

        assertFalse(events.isEmpty());
        assertEquals("Workshop", events.get(0).getTitle());
    }
}
