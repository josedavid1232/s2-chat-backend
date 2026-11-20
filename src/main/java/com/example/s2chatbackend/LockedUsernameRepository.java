package com.example.s2chatbackend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface LockedUsernameRepository extends JpaRepository<LockedUsernameEntity, Long> {
    Optional<LockedUsernameEntity> findByNormalizedUsername(String normalizedUsername);
    boolean existsByNormalizedUsername(String normalizedUsername);
}
