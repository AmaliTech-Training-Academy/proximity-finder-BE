package team.proximity.helpandsupport.contactsupport;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/support/contact-support")
public class ContactSupportController {

    private final ContactSupportService contactSupportService;

    public ContactSupportController(ContactSupportService contactSupportService) {
        this.contactSupportService = contactSupportService;
    }

    @PostMapping
    public ResponseEntity<String> contactSupport(@Valid @RequestBody ContactSupportRequest request) {
        contactSupportService.contactSupport(request);
        return ResponseEntity.ok("Support request sent successfully.");

    }
}
