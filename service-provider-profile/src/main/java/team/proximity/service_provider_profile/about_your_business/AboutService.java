package team.proximity.service_provider_profile.about_your_business;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface AboutService {

    void createOneAbout(AboutRequest aboutRequest, MultipartFile businessIdentityCardFile, MultipartFile businessCertificateFile) throws IOException;

}
