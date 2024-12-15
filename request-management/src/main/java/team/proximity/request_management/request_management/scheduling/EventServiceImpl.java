package team.proximity.request_management.request_management.scheduling;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
import team.proximity.request_management.request_management.exception.EventNotFoundException;
import team.proximity.request_management.request_management.exception.EventOverlapException;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    public EventServiceImpl(EventRepository eventRepository, EventMapper eventMapper) {
        this.eventRepository = eventRepository;
        this.eventMapper = eventMapper;
    }


    public void createEvent(EventRequest request) {
        Event event = eventMapper.mapToEvent(request);
        boolean existsOverlappingEvent = eventRepository.existsByStartDateAndTimeRange(
                event.getStartDate(),
                event.getStartTime(),
                event.getEndTime()
        );

        if (existsOverlappingEvent) {
            throw new EventOverlapException("An event already exists in the specified time range.");
        }
        eventRepository.save(event);
    }


    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findByEventIdAndCreatedBy(eventId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new EventNotFoundException("Event not found with id: " + eventId));
        return eventMapper.toEventResponse(event);
    }


    public List<EventResponse> getAllEvents() {
        return eventRepository.findByCreatedBy(SecurityContextUtils.getEmail())
                .stream()
                .map(eventMapper::toEventResponse)
                .collect(Collectors.toList());
    }


    public void updateEvent(Long eventId, EventRequest request) {
        Event event = eventRepository.findByEventIdAndCreatedBy(eventId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));

        event.setTitle(request.title());
        event.setStartDate(request.startDate());
        event.setStartTime(request.startTime());
        event.setEndDate(request.endDate());
        event.setEndTime(request.endTime());
        event.setDescription(request.description());

        eventRepository.save(event);

    }


    public void deleteEvent(Long eventId) {
        Event event = eventRepository.findByEventIdAndCreatedBy(eventId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
        eventRepository.delete(event);
    }


    public boolean isProviderAvailable(AvailabilityCheckRequest request) {

        List<Event> events = eventRepository.findEventsOnDate(request.schedulingDate(), request.createdBy());

        return events.isEmpty();
    }

}
