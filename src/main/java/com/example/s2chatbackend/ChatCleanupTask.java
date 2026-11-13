package com.example.s2chatbackend;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ChatCleanupTask {

    private final ChatRepository chatRepository;

    public ChatCleanupTask(ChatRepository chatRepository) {
        this.chatRepository = chatRepository;
    }

    // Se ejecuta automáticamente cada 60 segundos (1 minuto) sin interrumpir el chat activo
    @Scheduled(fixedRate = 60000)
    @Transactional
    public void cleanupOldMessages() {
        chatRepository.deleteExpiredMessages();
    }
}
