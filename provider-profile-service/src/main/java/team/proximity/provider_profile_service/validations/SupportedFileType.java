package team.proximity.provider_profile_service.validations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum SupportedFileType {
    JPEG("image/jpeg"),
    PNG("image/png"),
    PDF("application/pdf");

    private final String mimeType;

    public static Optional<SupportedFileType> fromMineType(String mimeType){
        return Arrays.stream(values())
                .filter(type -> type.getMimeType().equals(mimeType))
                .findFirst();
    }

}
