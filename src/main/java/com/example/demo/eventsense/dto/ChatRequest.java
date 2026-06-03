package com.example.demo.eventsense.dto;


import lombok.Data;
import java.util.List;

@Data
public class ChatRequest {
    private String userMessage;
    private String userEmail;

    public String getUserMessage() { return userMessage; }
    public void setUserMessage(String userMessage) { this.userMessage = userMessage; }

    public String getUserEmail() { return userEmail; }
    public void setUserEmail(String userEmail) { this.userEmail = userEmail; }
}