package team.proximity.provider_profile_service.about;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import team.proximity.provider_profile_service.common.ApiSuccessResponse;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/about")
public class AboutController {


    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<ApiSuccessResponse> createOneAbout(@ModelAttribute AboutRequest aboutRequest) throws IOException  {
        aboutService.createOneAbout(aboutRequest);

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("About created successfully", true));
    }

}
