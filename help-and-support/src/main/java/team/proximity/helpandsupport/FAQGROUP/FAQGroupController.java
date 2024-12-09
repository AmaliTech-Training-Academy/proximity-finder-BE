package team.proximity.helpandsupport.FAQGROUP;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.helpandsupport.FAQ.APISuccessResponse;

import java.util.List;

@RestController
@RequestMapping("/api/v1/support/faq-groups")
public class FAQGroupController {

    private final FAQGroupService faqGroupService;

    public FAQGroupController(FAQGroupService faqGroupService) {
        this.faqGroupService = faqGroupService;
    }


    @PostMapping
    public ResponseEntity<APISuccessResponse> createGroup(@RequestBody FAQGroupRequest request) {
        faqGroupService.createGroup(request);
        return ResponseEntity.ok(new APISuccessResponse("FAQ Group created successfully."));
    }

    @GetMapping
    public ResponseEntity<List<FAQGroupResponse>> getAllGroups() {
        return ResponseEntity.ok(faqGroupService.getAllGroups());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FAQGroupResponse> getGroupById(@PathVariable Long id) {
        return ResponseEntity.ok(faqGroupService.getGroupById(id));
    }
}
