package team.proximity.service_provider_profile.about_your_business.validations;

import lombok.Getter;

@Getter
public enum SupportedFileType {
    JPEG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf");

    private final String fileExtension;

    SupportedFileType(String fileExtension) {
        this.fileExtension = fileExtension;
    }


}
