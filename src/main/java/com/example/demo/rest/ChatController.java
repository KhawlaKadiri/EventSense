package com.example.demo.rest;


import com.example.demo.eventsense.dto.ChatRequest;
import com.example.demo.eventsense.dto.ChatResponse;
import com.example.demo.eventsense.repository.EventRepository;
import com.example.demo.eventsense.repository.UserRepository;
import com.example.demo.eventsense.service.RecommenderAgentService;
import com.example.demo.eventsense.service.RouterAgentService;
import com.example.demo.eventsense.service.CommonSenseAgentService;
import com.example.demo.model.Event;
import com.example.demo.model.Users;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequestMapping("/api/chat")
@CrossOrigin(origins = "*")
public class ChatController {

    private final RouterAgentService routerAgentService;
    private final RecommenderAgentService recommenderAgentService;
    private final UserRepository userRepository;  // NOT UsersRepository
    private final EventRepository eventRepository;
    private final CommonSenseAgentService commonSenseAgentService;

public ChatController(RouterAgentService routerAgentService,
                      RecommenderAgentService recommenderAgentService,
                      CommonSenseAgentService commonSenseAgentService,
                      UserRepository userRepository,
                      EventRepository eventRepository) {
    this.routerAgentService = routerAgentService;
    this.recommenderAgentService = recommenderAgentService;
    this.commonSenseAgentService = commonSenseAgentService;
    this.userRepository = userRepository;
    this.eventRepository = eventRepository;
}

    

    @PostMapping
    public ChatResponse chat(@RequestBody ChatRequest request) {
        String intent = routerAgentService.detectIntent(request.getUserMessage());
        ChatResponse response = new ChatResponse();
        response.setIntent(intent);

        if ("consult-event-list".equals(intent)) {
            // Get user preferences from database
            Users user = userRepository.findByEmail(request.getUserEmail())
                    .orElse(null);
            if (user == null) {
                user = userRepository.findById(1).orElse(null);
            }

            // Get events from database
            List<Event> events = eventRepository.findByAvailableSeatsGreaterThan(0);

            // Call recommender agent with user query + preferences + events
            String recommendation = recommenderAgentService.recommend(
                request.getUserMessage(),
                user,
                events
            );

            response.setResponse(recommendation);
        } else {
            String answer = commonSenseAgentService.answer(request.getUserMessage());
            response.setResponse(answer);

            
        }

        return response;
    }
}