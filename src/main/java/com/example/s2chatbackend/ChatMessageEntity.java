package com.example.s2chatbackend;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")
@Data
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String sender;

    @Column(length = 1000)
    private String content;

    private LocalDateTime timestamp = LocalDateTime.now();
}
