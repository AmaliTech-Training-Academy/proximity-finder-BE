package team.proximity.provider_profile_service.exception.about;

public class UserFileUploadException extends RuntimeException {
    public UserFileUploadException(String message) {
        super(message);
    }

    public UserFileUploadException(String message, Throwable cause) {
        super(message, cause);
    }
}

