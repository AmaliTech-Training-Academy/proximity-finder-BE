package team.proximity.management.controllers;

import team.proximity.management.requests.PreferenceRequest;
import team.proximity.management.model.Preference;
import team.proximity.management.services.PreferenceService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/preferences")
public class PreferenceController {
    private final PreferenceService preferenceService;

    public PreferenceController(PreferenceService preferenceService) {
        this.preferenceService = preferenceService;
    }

    @PostMapping
    public ResponseEntity<Preference> createPreference(@ModelAttribute PreferenceRequest preference) {
        Preference createdPreference = preferenceService.createPreference(preference);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPreference);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Preference> updatePreference(@PathVariable UUID id, @RequestBody PreferenceRequest preference) {
        Preference updatedPreference = preferenceService.updatePreference(id, preference);
        return ResponseEntity.ok(updatedPreference);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Preference> getPreferenceById(@PathVariable UUID id) {
        Optional<Preference> optionalPreference = preferenceService.getPreferenceById(id);
        return optionalPreference.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Preference>> getAllPreferences() {
        List<Preference> preferences = preferenceService.getAllPreferences();
        return ResponseEntity.ok(preferences);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable UUID id) {
        preferenceService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}