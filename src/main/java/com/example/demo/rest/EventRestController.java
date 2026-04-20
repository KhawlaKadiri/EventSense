package com.example.demo.rest;

import com.example.demo.eventsense.service.EventService;
import com.example.demo.model.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/events")
public class EventRestController {

    @Autowired
    private EventService eventService;

    // ===== CREATE EVENT =====
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Event> createEvent(@RequestBody Event event) {
        Event saved = eventService.saveEvent(event);
        // Retourne HTTP 201 Created avec l'événement créé
        return ResponseEntity.status(201).body(saved);
    }

    // ===== GET EVENT BY ID =====
    @GetMapping("/info/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        Optional<Event> eventOptional = eventService.findEventById(id);
        return eventOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ===== GET ALL EVENTS =====
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        List<Event> events = eventService.findAllEvents();
        return ResponseEntity.ok(events);
    }

    // ===== DELETE EVENT BY ID =====
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteEvent(@PathVariable int id) {
        Optional<Event> eventOptional = eventService.findEventById(id);
        if (eventOptional.isPresent()) {
            eventService.deleteEvent(id);
            return ResponseEntity.noContent().build();  // 204 No Content
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // ===== UPDATE EVENT =====
    @PutMapping("/update/{id}")
    public ResponseEntity<Event> updateEvent(@PathVariable int id, @RequestBody Event updatedEvent) {
        Optional<Event> eventOptional = eventService.updateEvent(id, updatedEvent);
        return eventOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ===== SEARCH EVENTS =====
@GetMapping("/search")
public ResponseEntity<List<Event>> searchEvents(
        @RequestParam(required = false) String title,
        @RequestParam(required = false) String category,
        @RequestParam(required = false) String location) {

    if (title != null)
        return ResponseEntity.ok(eventService.findByTitle(title));

    if (category != null)
        return ResponseEntity.ok(eventService.findByCategory(category));

    if (location != null)
        return ResponseEntity.ok(eventService.findByLocation(location));

    return ResponseEntity.ok(eventService.findAllEvents());
}
}
