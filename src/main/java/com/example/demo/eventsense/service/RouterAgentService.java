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
public class RouterAgentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String model;

    public RouterAgentService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private static final String SYSTEM_PROMPT = 
        "You are EventSense Router. Analyze the user message and output ONLY one word: consult-event-list or common-sense. " +
        "consult-event-list: user asks for events, concerts, festivals, matches, or anything requiring live data. " +
        "common-sense: general questions, help, how-to, policies, explanations. " +
        "Output exactly one word, no punctuation, no explanation.";

    // MUST RETURN String
    public String detectIntent(String userMessage) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", userMessage)
            ),
            "temperature", 0.1,
            "max_completion_tokens", 10,
            "stream", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(groqUrl, request, String.class);
        
        return extractIntent(response);
    }

    private String extractIntent(String groqResponse) {
        try {
            JsonNode root = objectMapper.readTree(groqResponse);
            String content = root.path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText()
                    .trim()
                    .toLowerCase();
            
            if (content.contains("consult-event-list")) {
                return "consult-event-list";
            } else {
                return "common-sense";
            }
        } catch (Exception e) {
            return "common-sense";
        }
    }
}