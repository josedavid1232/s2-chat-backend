package com.example.s2chatbackend;

public class ChatMessage {
    private Long id;
    private String sender;
    private String content;
    private MessageType type;
    private boolean edited;

    public enum MessageType {
        CHAT, JOIN, LEAVE, EDIT
    }

    public ChatMessage() {}

    public ChatMessage(String sender, String content, MessageType type) {
        this.sender = sender;
        this.content = content;
        this.type = type;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public MessageType getType() { return type; }
    public void setType(MessageType type) { this.type = type; }

    public boolean isEdited() { return edited; }
    public void setEdited(boolean edited) { this.edited = edited; }
}
