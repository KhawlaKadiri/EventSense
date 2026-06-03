package com.example.demo.rest;

import com.example.demo.eventsense.service.EventService;
import com.example.demo.eventsense.service.PersonService;
import com.example.demo.model.Event;
import com.example.demo.model.Person;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/events")
@CrossOrigin(origins = "*")
public class EventRestController {

    @Autowired
    private EventService eventService;

    @Autowired
    private PersonService personService;

    // ================= ADMIN ONLY =================

    // CREATE EVENT (ADMIN ONLY)
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<?> createEvent(@RequestParam int requesterId,
                                         @RequestBody Event event) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        return ResponseEntity.status(201).body(eventService.saveEvent(event));
    }

    // DELETE EVENT (ADMIN ONLY)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteEvent(@RequestParam int requesterId,
                                         @PathVariable int id) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        if (eventService.findEventById(id).isEmpty())
            return ResponseEntity.notFound().build();

        eventService.deleteEvent(id);
        return ResponseEntity.noContent().build();
    }

    // UPDATE EVENT (ADMIN ONLY)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateEvent(@RequestParam int requesterId,
                                         @PathVariable int id,
                                         @RequestBody Event updatedEvent) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        return eventService.updateEvent(id, updatedEvent)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ================= PUBLIC =================

    // GET EVENT BY ID
    @GetMapping("/info/{id}")
    public ResponseEntity<Event> getEventById(@PathVariable int id) {
        return eventService.findEventById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // GET ALL EVENTS
    @GetMapping("/all")
    public ResponseEntity<List<Event>> getAllEvents() {
        return ResponseEntity.ok(eventService.findAllEvents());
    }

    // SEARCH EVENTS
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