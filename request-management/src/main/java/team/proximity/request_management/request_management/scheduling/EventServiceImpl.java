package team.proximity.request_management.request_management.scheduling;

import org.springframework.stereotype.Service;
import jakarta.persistence.EntityNotFoundException;
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
        Event event = Event.builder()
                .title(request.title())
                .startDate(request.startDate())
                .startTime(request.startTime())
                .endDate(request.endDate())
                .endTime(request.endTime())
                .description(request.description())
                .createdBy(SecurityContextUtils.getEmail())
                .build();

        eventRepository.save(event);
    }


    public EventResponse getEvent(Long eventId) {
        Event event = eventRepository.findByEventIdAndCreatedBy(eventId, SecurityContextUtils.getEmail())
                .orElseThrow(() -> new EntityNotFoundException("Event not found with id: " + eventId));
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
