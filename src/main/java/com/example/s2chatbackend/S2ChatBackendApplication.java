package com.example.s2chatbackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.beans.factory.annotation.Autowired;

@SpringBootApplication
@EnableScheduling
public class S2ChatBackendApplication {

    @Autowired
    private ChatRepository chatRepository;

    public static void main(String[] args) {
        SpringApplication.run(S2ChatBackendApplication.class, args);
    }

    // Tarea automática que borra la base de datos de Aiven cada 30 minutos
    @Scheduled(fixedRate = 1800000)
    public void limpiarChatAntiguo() {
        try {
            chatRepository.deleteAll();
            System.out.println("🧹 Historial de chat limpiado automáticamente tras 30 minutos.");
        } catch (Exception e) {
            System.err.println("Error al limpiar el chat automático: " + e.getMessage());
        }
    }

}
