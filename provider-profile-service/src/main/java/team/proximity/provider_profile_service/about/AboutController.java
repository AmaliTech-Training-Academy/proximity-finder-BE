package team.proximity.provider_profile_service.about;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import team.proximity.provider_profile_service.common.ApiSuccessResponse;

@RestController
@RequestMapping("/api/v1/provider-service/about")
public class AboutController {


    private final AboutService aboutService;

    public AboutController(AboutService aboutService) {
        this.aboutService = aboutService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<ApiSuccessResponse> createOneAbout(@Valid @ModelAttribute AboutRequest aboutRequest){

            aboutService.createOneAbout(aboutRequest);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(new ApiSuccessResponse("About Company Created Successfully", true));
    }

    @GetMapping("/about-company")
    public ResponseEntity<AboutBusinessResponse> getAboutForAuthenticatedUser() {
        AboutBusinessResponse response = aboutService.getAboutForAuthenticatedUser();
        return ResponseEntity.ok(response);
    }

    @GetMapping("/provider-profile")
    public ResponseEntity<AboutAndPaymentMethodsResponse> getAboutAndPaymentMethods(@RequestParam String email) {
        AboutAndPaymentMethodsResponse response = aboutService.getAboutAndPaymentMethods(email);
        return ResponseEntity.ok(response);
    }
}