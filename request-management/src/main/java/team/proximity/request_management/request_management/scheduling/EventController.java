package team.proximity.request_management.request_management.scheduling;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.request_management.request_management.quotes.ApiSuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quote-service/events")

public class EventController {

    private final EventService eventService;

    public EventController(EventService eventService) {
        this.eventService = eventService;
    }


    @PostMapping
    public ResponseEntity<ApiSuccessResponse> createEvent(@RequestBody EventRequest request) {
        eventService.createEvent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("Event created successfully"));
    }


    @GetMapping("/{eventId}")
    public ResponseEntity<EventResponse> getEvent(@PathVariable Long eventId) {
        EventResponse eventResponse = eventService.getEvent(eventId);
        return ResponseEntity.ok(eventResponse);
    }


    @GetMapping
    public ResponseEntity<List<EventResponse>> getAllEvents() {
        List<EventResponse> events = eventService.getAllEvents();
        return ResponseEntity.ok(events);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<ApiSuccessResponse> updateEvent(@PathVariable Long eventId, @RequestBody EventRequest request) {
        eventService.updateEvent(eventId, request);
        return ResponseEntity.ok(new ApiSuccessResponse("Event updated successfully"));
    }


    @DeleteMapping("/{eventId}")
    public ResponseEntity<ApiSuccessResponse> deleteEvent(@PathVariable Long eventId) {
        eventService.deleteEvent(eventId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT)
                .body(new ApiSuccessResponse("Event deleted successfully"));
    }
}