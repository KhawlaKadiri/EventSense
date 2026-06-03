package com.example.demo.eventsense.service;



import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.Map;

@Service
public class CommonSenseAgentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String model;

    public CommonSenseAgentService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private static final String SYSTEM_PROMPT = 
        "You are EventSense Assistant, a helpful guide for the EventSense Event Management and Ticketing Platform in Morocco. " +
        "You answer general questions about how the app works, policies, and user support. " +
        "DETECT the user's language from their message and RESPOND ONLY in that same language (English, French, or Darija). " +
        "Do NOT mix languages. Do NOT translate. Do NOT use newlines. Output everything on one single line. " +
        "Keep responses brief (max 3 sentences). Current date: 2026-06-02.";

    public String answer(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userMessage)
            ),
            "temperature", 0.3,
            "max_completion_tokens", 150,
            "stream", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(groqUrl, request, String.class);
        
        return cleanResponse(response);
    }

    private String cleanResponse(String groqResponse) {
        try {
            JsonNode root = objectMapper.readTree(groqResponse);
            String content = root.path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim()
                    .replace("\n", " ")
                    .replace("\r", " ")
                    .replaceAll("\\s+", " ");
            
            return content;
        } catch (Exception e) {
            return "Sorry, error processing your request.";
        }
    }
}