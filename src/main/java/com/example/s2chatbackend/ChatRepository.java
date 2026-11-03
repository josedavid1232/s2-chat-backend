package com.example.s2chatbackend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findAllByOrderByIdAsc();
}
