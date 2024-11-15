package team.proximity.provider_profile_service.about;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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
    public ResponseEntity<ApiSuccessResponse> createOneAbout( @Valid @ModelAttribute AboutRequest aboutRequest) throws IOException {
        aboutService.createOneAbout(aboutRequest);

        return  ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new ApiSuccessResponse("About created successfully", true));
    }



}
