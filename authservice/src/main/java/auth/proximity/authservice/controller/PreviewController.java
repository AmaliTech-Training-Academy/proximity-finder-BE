package auth.proximity.authservice.controller;

import auth.proximity.authservice.services.PreviewService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/preview")
public class PreviewController {
private final PreviewService previewService;
    public PreviewController(PreviewService previewService) {
        this.previewService = previewService;
    }

    @GetMapping("")
    public Mono<Map<String, Object>> getPreview(@RequestParam String userEmail  ) {
        return previewService.getAggregatedData(userEmail);
    }
}
