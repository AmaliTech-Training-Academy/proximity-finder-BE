package team.proximity.service_provider_profile.about_your_business;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/about")
public class AboutController {


    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<String> createOneAbout(
            @Validated @RequestPart("aboutRequest") AboutRequest aboutRequest,
            @RequestPart("businessIdentityCardFile") MultipartFile businessIdentityCardFile,
            @RequestPart("businessCertificateFile") MultipartFile businessCertificateFile) throws IOException {

        aboutService.createOneAbout(aboutRequest, businessIdentityCardFile, businessCertificateFile);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("About entity created successfully.");
    }

}
