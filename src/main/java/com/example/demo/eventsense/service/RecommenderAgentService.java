package com.example.demo.eventsense.service;


import com.example.demo.model.Event;
import com.example.demo.model.Users;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;
import java.util.Map;

@Service
public class RecommenderAgentService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;

    @Value("${groq.api.key}")
    private String groqApiKey;

    @Value("${groq.api.url}")
    private String groqUrl;

    @Value("${groq.model}")
    private String model;

    public RecommenderAgentService() {
        this.restTemplate = new RestTemplate();
        this.objectMapper = new ObjectMapper();
    }

    private static final String SYSTEM_PROMPT = 
        "You are EventSense Recommender. You receive a user's query, their profile preferences, and a list of events from the database. " +
        "Your job is to recommend the ONE event that best fits the user's request and preferences. " +
        "CRITICAL RULES: " +
        "1. If user mentions their age (like 'i have 11 years'), ONLY recommend events where age_min <= user_age <= age_max. " +
        "2. If user mentions an actor/artist name, match with events where actors field contains that name. " +
        "3. If user mentions a category, match with events in that category. " +
        "4. Match user budget with event price. " +
        "5. Match preferred_locations with event location. " +
        "Output ONLY this exact format (replace placeholders with real values): " +
        "The event that best matches your request is Event #ID, titled \"TITLE\", as it meets your requirements because REASON. " +
        "If no event matches age requirement, output: Sorry, no event found for your age. " +
        "If no event matches at all, output: Sorry, no matching event found.";

    public String recommend(String userQuery, Users user, List<Event> events) {
        String userPreferences = formatUserPreferences(user);
        String eventList = formatEvents(events);

        String content = String.format(
            "User query: %s\n\n" +
            "User preferences:\n%s\n\n" +
            "Available events:\n%s\n\n" +
            "Recommend the best event. STRICTLY follow age rules.",
            userQuery, userPreferences, eventList
        );

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(groqApiKey);

        Map<String, Object> requestBody = Map.of(
            "model", model,
            "messages", List.of(
                Map.of("role", "system", "content", SYSTEM_PROMPT),
                Map.of("role", "user", "content", content)
            ),
            "temperature", 0.1,
            "max_completion_tokens", 200,
            "stream", false
        );

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(requestBody, headers);
        String response = restTemplate.postForObject(groqUrl, request, String.class);
        
        return extractRecommendation(response);
    }

    private String formatUserPreferences(Users user) {
        if (user == null) return "- No user profile found\n";
        
        StringBuilder sb = new StringBuilder();
        sb.append("- Age: ").append(user.getAge()).append("\n");
        sb.append("- Budget max: ").append(user.getBudgetMax()).append(" MAD\n");
        sb.append("- Preferred categories: ").append(user.getPreferredCategories()).append("\n");
        sb.append("- Preferred locations: ").append(user.getPreferredLocations()).append("\n");
        sb.append("- Preferred actors: ").append(user.getPreferredActors()).append("\n");
        return sb.toString();
    }

    private String formatEvents(List<Event> events) {
        StringBuilder sb = new StringBuilder();
        for (Event e : events) {
            sb.append(String.format(
                "ID: %d | Title: %s | Category: %s | Location: %s | Date: %s | Price: %.0f MAD | Age: %d-%d | Seats: %d | Actors: %s\n",
                e.getId(), e.getTitle(), e.getCategory(), e.getLocation(),
                e.getEventDate(), e.getPrice(), e.getAgeMin(), e.getAgeMax(),
                e.getAvailableSeats(), e.getActors()
            ));
        }
        return sb.toString();
    }

    private String extractRecommendation(String groqResponse) {
        try {
            JsonNode root = objectMapper.readTree(groqResponse);
            return root.path("choices").get(0)
                    .path("message")
                    .path("content")
                    .asText();
        } catch (Exception e) {
            return "Sorry, error processing recommendation.";
        }
    }
}