package com.example.s2chatbackend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessageEntity, Long> {
    // Opcional: Para traer los últimos mensajes ordenados
    List<ChatMessageEntity> findTop50ByOrderByIdAsc();
}
