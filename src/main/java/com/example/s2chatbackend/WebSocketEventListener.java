package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Usamos un ConcurrentHashMap seguro para hilos para almacenar los IDs de las sesiones activas
    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        String sessionId = (String) event.getMessage().getHeaders().get("simpSessionId");
        if (sessionId != null) {
            activeSessions.add(sessionId);
        }
        broadcastUserCount();
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        String sessionId = event.getSessionId();
        if (sessionId != null) {
            activeSessions.remove(sessionId);
        }
        broadcastUserCount();
    }

    private void broadcastUserCount() {
        int count = activeSessions.size();
        // Envía el tamaño exacto de sesiones activas al tópico público
        messagingTemplate.convertAndSend("/topic/users", count);
    }
}
