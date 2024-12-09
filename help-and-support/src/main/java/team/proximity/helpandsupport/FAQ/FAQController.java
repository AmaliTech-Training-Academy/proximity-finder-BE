package team.proximity.helpandsupport.FAQ;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/v1/support/faqs")
public class FAQController {

    private final FAQService faqService;

    public FAQController(FAQService faqService) {
        this.faqService = faqService;
    }


    @PostMapping
    public ResponseEntity<APISuccessResponse> createFAQ(@RequestBody FAQRequest faqRequest) {
        faqService.createFAQ(faqRequest);
        return ResponseEntity.ok(new APISuccessResponse("FAQ created successfully."));
    }
    @GetMapping("/group")
    public List<FAQResponse> getFAQs(@RequestParam(name = "type", required = false) String type) {
        return faqService.getFAQsForType(type);
    }


    @PutMapping("/{id}")
    public ResponseEntity<APISuccessResponse> updateFAQ(@PathVariable Long id, @RequestBody FAQRequest faqRequest) {
        faqService.updateFAQ(id, faqRequest);
        return ResponseEntity.ok(new APISuccessResponse("FAQ updated successfully."));
    }

    @GetMapping
    public ResponseEntity<List<FAQResponse>> getAllFAQs() {
        return ResponseEntity.ok(faqService.getAllFAQs());
    }

    @GetMapping("/group/{groupId}")
    public ResponseEntity<List<FAQResponse>> getFAQsByGroup(@PathVariable Long groupId) {
        return ResponseEntity.ok(faqService.getFAQsByGroup(groupId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<FAQResponse> getFAQById(@PathVariable Long id) {
        return ResponseEntity.ok(faqService.getFAQById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<APISuccessResponse> deleteFAQ(@PathVariable Long id) {
        faqService.deleteFAQ(id);
        return ResponseEntity.ok(new APISuccessResponse("FAQ deleted successfully."));
    }
}
