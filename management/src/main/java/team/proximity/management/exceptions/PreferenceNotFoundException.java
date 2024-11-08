package team.proximity.management.exceptions;

import java.util.UUID;

public class PreferenceNotFoundException extends RuntimeException {
    public PreferenceNotFoundException(UUID id) {
        super("Preference with ID " + id + " not found.");
    }
}
