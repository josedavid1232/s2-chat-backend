package com.example.s2chatbackend.model;

public class ChatMessage {
    private String sender;
    private String content;
    private String type;

    // Getters y Setters
    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }
    
    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }
    
    public String type() { return type; }
    public void setType(String type) { this.type = type; }
}
