package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Almacena los IDs de las sesiones activas de forma segura
    private final Set<String> activeSessions = ConcurrentHashMap.newKeySet();

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String sessionId = headerAccessor.getSessionId();
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

    // NUEVO: Cada vez que un cliente se suscribe al canal, le enviamos el conteo actual al instante
    @EventListener
    public void handleSessionSubscribeListener(SessionSubscribeEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
        String destination = headerAccessor.getDestination();
        
        if ("/topic/users".equals(destination)) {
            broadcastUserCount();
        }
    }

    private void broadcastUserCount() {
        int count = activeSessions.size();
        messagingTemplate.convertAndSend("/topic/users", count);
    }
}
