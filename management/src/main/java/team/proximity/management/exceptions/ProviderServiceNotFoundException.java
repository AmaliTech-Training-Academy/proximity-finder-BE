package team.proximity.management.exceptions;

import java.util.UUID;

public class ProviderServiceNotFoundException extends RuntimeException {
    public ProviderServiceNotFoundException(UUID id) {
        super("Preference with ID " + id + " not found.");
    }
}
