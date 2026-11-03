package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.concurrent.atomic.AtomicInteger;

@Component
public class WebSocketEventListener {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    // Contador atómico seguro para múltiples usuarios concurrentes
    private final AtomicInteger activeUsers = new AtomicInteger(0);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        int count = activeUsers.incrementAndGet();
        broadcastUserCount(count);
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        int count = Math.max(0, activeUsers.decrementAndGet());
        broadcastUserCount(count);
    }

    private void broadcastUserCount(int count) {
        // Envía el número de conectados al tópico público
        messagingTemplate.convertAndSend("/topic/users", count);
    }
}
