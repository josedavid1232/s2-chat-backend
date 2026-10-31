package com.example.s2chatbackend;

import jakarta.persistence.*; // Asegúrate de usar jakarta si estás en Spring Boot 3+

@Entity
@Table(name = "messages")
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;
    private String content;
    
    @Enumerated(EnumType.STRING)
    private MessageType type;

    public enum MessageType {
        CHAT, JOIN, LEAVE
    }

    // Constructores, Getters y Setters...
}
