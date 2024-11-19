package team.proximity.provider_profile_service.about;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.provider_profile_service.common.ApiSuccessResponse;

import java.io.IOException;
@Tag(name = "About Company", description = "Operations related to about company")
@RestController
@RequestMapping("/api/v1/about")
public class AboutController {


    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @Operation(summary = "Create About Company")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiSuccessResponse> createOneAbout(@Valid @ModelAttribute AboutRequest aboutRequest) throws IOException {

            aboutService.createOneAbout(aboutRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiSuccessResponse("About Company Created Successfully", true));
    }
    @Operation(summary = "Get About Company for the authenticated user")
    @SecurityRequirement(name = "BearerAuth")
    @GetMapping("/about-company")
    public ResponseEntity<AboutBusinessResponse> getAboutForAuthenticatedUser() {
        AboutBusinessResponse response = aboutService.getAboutForAuthenticatedUser();
        return ResponseEntity.ok(response);
    }
}