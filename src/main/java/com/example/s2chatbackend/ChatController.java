package com.example.s2chatbackend.controller;

import com.example.s2chatbackend.model.ChatMessage;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    // Escucha cuando el frontend envía un mensaje a /app/chat.sendMessage
    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public") // Reenvía automáticamente el mensaje a todos los suscritos
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        return chatMessage;
    }
}
