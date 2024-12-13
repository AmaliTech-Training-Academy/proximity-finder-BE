package team.proximity.request_management.request_management.scheduling;

import org.springframework.stereotype.Component;
import team.proximity.request_management.request_management.security.SecurityContextUtils;

@Component
public class EventMapper {

    public Event mapToEvent(EventRequest request){
        return Event.builder()
                .title(request.title())
                .startDate(request.startDate())
                .startTime(request.startTime())
                .endDate(request.endDate())
                .endTime(request.endTime())
                .description(request.description())
                .createdBy(SecurityContextUtils.getEmail())
                .build();
    }

    public EventResponse toEventResponse(Event event){
        return new EventResponse(
                event.getEventId(),
                event.getTitle(),
                event.getStartDate(),
                event.getStartTime(),
                event.getEndDate(),
                event.getEndTime(),
                event.getDescription(),
                event.getCreatedBy()
        );
    }

}
