package team.proximity.provider_profile_service.about;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/about")
public class AboutController {


    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> createOneAbout(@ModelAttribute AboutRequest aboutRequest) throws IOException  {
        aboutService.createOneAbout(aboutRequest);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body("About entity created successfully.");
    }

}
