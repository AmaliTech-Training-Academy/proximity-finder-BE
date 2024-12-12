package team.proximity.request_management.request_management.scheduling;

import java.util.List;

public interface EventService {
    void createEvent(EventRequest request);

    EventResponse getEvent(Long eventId);

    List<EventResponse> getAllEvents();

    void updateEvent(Long eventId, EventRequest request);

    void deleteEvent(Long eventId);

    boolean isProviderAvailable(AvailabilityCheckRequest request);
}
