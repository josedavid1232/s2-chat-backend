package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public List<String> getLockedUsernames() {
        return repository.findAll()
                .stream()
                .map(LockedUsernameEntity::getNormalizedUsername)
                .toList();
    }

    @PostMapping("/lock")
    public ResponseEntity<?> lockUsername(@RequestBody Map<String, String> body) {
        String rawName = body.get("username");
        if (rawName == null || rawName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "El nombre no puede estar vacío"));
        }

        String cleanDisplay = rawName.trim();
        String normalized = cleanDisplay.toLowerCase();

        if (repository.existsByNormalizedUsername(normalized)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("error", "Ese nombre ya está bloqueado por otro usuario"));
        }

        LockedUsernameEntity entity = new LockedUsernameEntity();
        entity.setDisplayUsername(cleanDisplay);
        entity.setNormalizedUsername(normalized);
        repository.save(entity);

        return ResponseEntity.ok(Map.of("username", cleanDisplay, "locked", true));
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
