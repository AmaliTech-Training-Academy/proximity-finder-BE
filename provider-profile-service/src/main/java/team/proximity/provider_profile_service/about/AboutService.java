package team.proximity.provider_profile_service.about;

import java.io.IOException;

public interface AboutService {

    void createOneAbout(AboutRequest aboutRequest);
    AboutBusinessResponse getAboutForAuthenticatedUser();
    AboutAndPaymentMethodsResponse getAboutAndPaymentMethods(String email);

}
