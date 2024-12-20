package team.proximity.provider_profile_service.about;

import java.io.IOException;

public interface AboutService {

    void createAbout(AboutRequest aboutRequest);
    AboutBusinessResponse getAboutForAuthenticatedUser();
    AboutAndPaymentMethodsResponse getAboutAndPaymentMethods(String email);

}
