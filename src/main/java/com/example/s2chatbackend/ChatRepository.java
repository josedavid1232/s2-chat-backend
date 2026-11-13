package com.example.s2chatbackend;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

public interface ChatRepository extends JpaRepository<ChatMessageEntity, Long> {

    // Método original intacto que ya usaba tu chat
    List<ChatMessageEntity> findAllByOrderByIdAsc();

    // Nueva adición: Borrado progresivo de mensajes con más de 30 minutos
    @Modifying
    @Transactional
    @Query(value = "DELETE FROM chat_message_entity WHERE timestamp < NOW() - INTERVAL 30 MINUTE", nativeQuery = true)
    void deleteExpiredMessages();

}
