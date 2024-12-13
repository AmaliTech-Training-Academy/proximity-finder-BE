package team.proximity.request_management.request_management.scheduling;

import org.springframework.stereotype.Component;

@Component
public class EventMapper {

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
