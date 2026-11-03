package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // Crear la entidad para la base de datos
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSender(chatMessage.getSender());
        entity.setContent(chatMessage.getContent());
        entity.setTimestamp(LocalDateTime.now());
        
        // Guardar el mensaje en MySQL
        chatRepository.save(entity);

        return chatMessage;
    }

    // Endpoint opcional para cargar el historial inicial de mensajes
    @GetMapping("/messages")
    @ResponseBody
    public List<ChatMessageEntity> getMessages() {
        return chatRepository.findAllByOrderByIdAsc();
    }
}
