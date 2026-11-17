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
        ChatMessageEntity entity = new ChatMessageEntity();
        entity.setSender(chatMessage.getSender());
        entity.setContent(chatMessage.getContent());
        entity.setTimestamp(LocalDateTime.now());
        entity.setEdited(false);
        
        ChatMessageEntity savedEntity = chatRepository.save(entity);

        chatMessage.setId(savedEntity.getId());
        chatMessage.setEdited(false);

        return chatMessage;
    }

    @MessageMapping("/chat.editMessage")
    public void editMessage(@Payload ChatMessage editPayload) {
        if (editPayload.getId() != null) {
            Optional<ChatMessageEntity> optionalEntity = chatRepository.findById(editPayload.getId());

            if (optionalEntity.isPresent()) {
                ChatMessageEntity entity = optionalEntity.get();
                entity.setContent(editPayload.getContent());
                entity.setEdited(true);

                chatRepository.save(entity);

                editPayload.setEdited(true);
                messagingTemplate.convertAndSend("/topic/public", editPayload);
            }
        }
    }

    @MessageMapping("/chat.deleteMessage")
    public void deleteMessage(@Payload ChatMessage deletePayload) {
        if (deletePayload.getId() != null) {
            chatRepository.deleteById(deletePayload.getId());

            deletePayload.setType("DELETE");
            messagingTemplate.convertAndSend("/topic/public", deletePayload);
        }
    }

    @GetMapping("/messages")
    @ResponseBody
    public List<ChatMessageEntity> getMessages() {
        return chatRepository.findAllByOrderByIdAsc();
    }
}
