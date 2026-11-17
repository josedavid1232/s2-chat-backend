package com.example.s2chatbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Controller
@CrossOrigin(origins = "*")
public class ChatController {

    @Autowired
    private ChatRepository chatRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @MessageMapping("/chat.sendMessage")
    @SendTo("/topic/public")
    public ChatMessage sendMessage(ChatMessage chatMessage) {
        // Crear la entidad para la base de datos
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSender(chatMessage.getSender());
        entity.setContent(chatMessage.getContent());
        entity.setTimestamp(LocalDateTime.now());
        entity.setEdited(false);
        
        // Guardar el mensaje en MySQL
        ChatMessageEntity savedEntity = chatRepository.save(entity);

        // Devolver el mensaje con el ID asignado por la base de datos
        chatMessage.setId(savedEntity.getId());
        chatMessage.setEdited(false);

        return chatMessage;
    }

    @MessageMapping("/chat.editMessage")
    public void editMessage(@Payload ChatMessage editPayload) {
        if (editPayload.getId() != null) {
            // Buscar el mensaje original en la base de datos por su ID
            Optional<ChatMessageEntity> optionalEntity = chatRepository.findById(editPayload.getId());

            if (optionalEntity.isPresent()) {
                ChatMessageEntity entity = optionalEntity.get();
                
                // Actualizar el contenido y la bandera de edición
                entity.setContent(editPayload.getContent());
                entity.setEdited(true);

                // Guardar los cambios permanentemente en MySQL
                chatRepository.save(entity);

                // Preparar la respuesta para el WebSocket
                editPayload.setEdited(true);

                // Retransmitir la actualización a todos los clientes conectados
                messagingTemplate.convertAndSend("/topic/public", editPayload);
            }
        }
    }

    // Endpoint opcional para cargar el historial inicial de mensajes
    @GetMapping("/messages")
    @ResponseBody
    public List<ChatMessageEntity> getMessages() {
        return chatRepository.findAllByOrderByIdAsc();
    }
}
