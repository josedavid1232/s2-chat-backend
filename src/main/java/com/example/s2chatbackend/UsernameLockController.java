package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/usernames")
@CrossOrigin(origins = "*")
public class UsernameLockController {

    @Autowired
    private LockedUsernameRepository repository;

    @GetMapping("/locked")
    public Map<String, String> getLockedUsernames() {
        List<LockedUsernameEntity> all = repository.findAll();
        Map<String, String> lockedColors = new HashMap<>();
        for (LockedUsernameEntity entity : all) {
            lockedColors.put(entity.getNormalizedUsername(), entity.getColor());
        }
        return lockedColors;
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockUsername(@RequestBody Map<String, String> body) {
        String rawName = body.get("username");
        if (rawName == null || rawName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre no puede estar vacío"));
        }

        String cleanDisplay = rawName.trim();
        String normalized = cleanDisplay.toLowerCase();
        String requestedColor = body.get("color");
        String finalColor = (requestedColor != null && requestedColor.matches("^#[0-9a-fA-F]{6}$"))
                ? requestedColor
                : "#58a6ff";

        Optional<LockedUsernameEntity> existing = repository.findByNormalizedUsername(normalized);
        if (existing.isPresent()) {
            LockedUsernameEntity current = existing.get();
            if ("true".equalsIgnoreCase(body.get("updateColor"))) {
                current.setColor(finalColor);
                repository.save(current);
                return ResponseEntity.ok(Map.of(
                        "username", current.getDisplayUsername(),
                        "color", current.getColor(),
                        "locked", true
                ));
            }
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ese nombre ya está bloqueado por otro usuario"));
        }

        LockedUsernameEntity entity = new LockedUsernameEntity();
        entity.setDisplayUsername(cleanDisplay);
        entity.setNormalizedUsername(normalized);
        entity.setColor(finalColor);
        repository.save(entity);

        return ResponseEntity.ok(Map.of(
                "username", cleanDisplay,
                "color", finalColor,
                "locked", true
        ));
    }

    @PostMapping("/release")
    public ResponseEntity<?> releaseUsername(@RequestBody Map<String, String> body) {
        String rawName = body.get("username");
        if (rawName == null || rawName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Nombre inválido"));
        }

        String normalized = rawName.trim().toLowerCase();
        Optional<LockedUsernameEntity> existing = repository.findByNormalizedUsername(normalized);
        existing.ifPresent(repository::delete);

        return ResponseEntity.ok(Map.of("username", rawName.trim(), "locked", false));
    }
}
