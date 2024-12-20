package team.proximity.request_management.request_management.scheduling;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import team.proximity.request_management.request_management.exception.EventNotFoundException;
import team.proximity.request_management.request_management.exception.EventOverlapException;

import java.util.Optional;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    private final EventRepository eventRepository = mock(EventRepository.class);
    private final EventMapper eventMapper = mock(EventMapper.class);
    private final EventServiceImpl eventService = new EventServiceImpl(eventRepository, eventMapper);

    @Test
    void shouldCreateEventSuccessfully() {
        EventRequest request = new EventRequest(
                "Conference",
                "2024-12-23",
                "14:00",
                "2024-12-23",
                "16:00",
                "Annual conference"
        );
        Event event = Event.builder().build();

        when(eventMapper.mapToEvent(request)).thenReturn(event);
        when(eventRepository.existsByStartDateAndTimeRange("2024-12-23", "14:00", "16:00")).thenReturn(false);

        eventService.createEvent(request);

        verify(eventRepository).save(event);
    }

    @Test
    void shouldThrowExceptionForOverlappingEvent() {
        EventRequest request = new EventRequest(
                "Meeting",
                "2024-12-24",
                "10:00",
                "2024-12-24",
                "11:00",
                "Team sync"
        );

        when(eventRepository.existsByStartDateAndTimeRange("2024-12-24", "10:00", "11:00")).thenReturn(true);

        assertThrows(EventOverlapException.class, () -> eventService.createEvent(request));

        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void shouldGetEventSuccessfully() {
        Event event = Event.builder()
                .eventId(1L)
                .title("Workshop")
                .createdBy("test@example.com")
                .build();

        when(eventRepository.findByEventIdAndCreatedBy(1L, "test@example.com")).thenReturn(Optional.of(event));
        when(eventMapper.toEventResponse(event)).thenReturn(new EventResponse(
                1L, "Workshop", "2024-12-25", "09:00", "2024-12-25", "11:00", "Details", "test@example.com"
        ));

        EventResponse response = eventService.getEvent(1L);

        assertEquals(1L, response.eventId());
        assertEquals("Workshop", response.title());
    }

    @Test
    void shouldThrowEventNotFound() {
        when(eventRepository.findByEventIdAndCreatedBy(1L, "test@example.com")).thenReturn(Optional.empty());

        assertThrows(EventNotFoundException.class, () -> eventService.getEvent(1L));
    }
}
