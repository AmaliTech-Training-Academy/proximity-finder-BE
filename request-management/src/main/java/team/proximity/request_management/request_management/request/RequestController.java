package team.proximity.request_management.request_management.request;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/quote-service/requests")
public class RequestController {

    private final RequestService requestService;

    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @GetMapping("/assigned")
    public ResponseEntity<List<RequestResponse>> getAssignedRequests() {
        List<RequestResponse> assignedRequests = requestService.findAssignedRequests();

        return new ResponseEntity<>(assignedRequests, HttpStatus.OK);
    }

}
