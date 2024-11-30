package team.proximity.request_management.request_management.validations;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Optional;

@AllArgsConstructor
@Getter
public enum SupportedFileType {
    JPEG("image/jpeg"),
    PNG("image/png");


    private final String mimeType;

    public static Optional<SupportedFileType> fromMineType(String mimeType){
        return Arrays.stream(values())
                .filter(type -> type.getMimeType().equals(mimeType))
                .findFirst();
    }

}
