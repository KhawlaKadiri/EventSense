package com.example.demo.rest;

import com.example.demo.eventsense.service.TicketService;
import com.example.demo.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/tickets")
@CrossOrigin(origins = "*")
public class TicketRestController {

    @Autowired
    private TicketService ticketService;

    // ===== CREATE TICKET =====
    @PostMapping("/create")
public ResponseEntity<?> createTicket(@RequestBody Ticket ticket) {
    try {
        return ResponseEntity.status(201).body(ticketService.saveTicket(ticket));
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    // ===== GET BY ID =====
    @GetMapping("/info/{id}")
    public ResponseEntity<Ticket> getTicketById(@PathVariable int id) {
        Optional<Ticket> ticket = ticketService.findTicketById(id);
        return ticket.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ===== GET ALL =====
    @GetMapping("/all")
    public ResponseEntity<List<Ticket>> getAllTickets() {
        return ResponseEntity.ok(ticketService.findAllTickets());
    }

    // ===== DELETE =====
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int id) {
        Optional<Ticket> ticket = ticketService.findTicketById(id);

        if (ticket.isPresent()) {
            ticketService.deleteTicket(id);
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.notFound().build();
    }

    // ===== UPDATE =====
    @PutMapping("/update/{id}")
    public ResponseEntity<Ticket> updateTicket(
            @PathVariable int id,
            @RequestBody Ticket updatedTicket) {

        Optional<Ticket> ticket = ticketService.updateTicket(id, updatedTicket);

        return ticket.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // ===== BY USER =====
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Ticket>> getByUser(@PathVariable int userId) {
        return ResponseEntity.ok(ticketService.findByUser(userId));
    }

    // ===== BY EVENT =====
    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<Ticket>> getByEvent(@PathVariable int eventId) {
        return ResponseEntity.ok(ticketService.findByEvent(eventId));
    }
}