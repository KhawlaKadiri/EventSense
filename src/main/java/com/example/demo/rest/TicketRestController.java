package com.example.demo.rest;

import com.example.demo.eventsense.repository.TicketRepository;
import com.example.demo.eventsense.service.PersonService;
import com.example.demo.eventsense.service.TicketService;
import com.example.demo.model.Person;
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

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private PersonService personService;

    // ================= HELPER =================
    private boolean isAdmin(int requesterId) {
        return personService.findPersonById(requesterId)
                .map(p -> "ROLE_ADMIN".equals(p.getRole()))
                .orElse(false);
    }

    private boolean isAdminOrSelf(int requesterId, int targetUserId) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty()) return false;
        return "ROLE_ADMIN".equals(requester.get().getRole())
                || requesterId == targetUserId;
    }

    // =========================
    // CREATE — any authenticated user (for themselves)
    // POST /rest/tickets/create?requesterId=5
    // =========================
    @PostMapping("/create")
    public ResponseEntity<?> createTicket(
            @RequestParam int requesterId,
            @RequestBody Ticket ticket
    ) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");

        // user can only book for themselves unless admin
        if (!isAdminOrSelf(requesterId, ticket.getUserId()))
            return ResponseEntity.status(403).body("Access denied: you can only book for yourself");

        System.out.println("RECEIVED: " + ticket);

        try {
            return ResponseEntity.status(201).body(ticketService.saveTicket(ticket));
        } catch (RuntimeException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================
    // GET BY ID — admin or ticket owner
    // GET /rest/tickets/info/{id}?requesterId=5
    // =========================
    @GetMapping("/info/{id}")
    public ResponseEntity<?> getTicketById(
            @PathVariable int id,
            @RequestParam int requesterId
    ) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");

        Optional<Ticket> ticket = ticketService.findTicketById(id);
        if (ticket.isEmpty())
            return ResponseEntity.notFound().build();

        // admin can see any ticket; user can only see their own
        boolean isAdmin = "ROLE_ADMIN".equals(requester.get().getRole());
        boolean isOwner = ticket.get().getUserId() == requesterId;

        if (!isAdmin && !isOwner)
            return ResponseEntity.status(403).body("Access denied: not your ticket");

        return ResponseEntity.ok(ticket.get());
    }

    // =========================
    // GET ALL — admin only
    // GET /rest/tickets/all?requesterId=2
    // =========================
    @GetMapping("/all")
    public ResponseEntity<?> getAllTickets(
            @RequestParam int requesterId
    ) {
        if (!isAdmin(requesterId))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        return ResponseEntity.ok(ticketService.findAllTickets());
    }

    // =========================
    // DELETE — admin only
    // DELETE /rest/tickets/delete/{id}?requesterId=2
    // =========================
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteTicket(
            @PathVariable int id,
            @RequestParam int requesterId
    ) {
        if (!isAdmin(requesterId))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        Optional<Ticket> ticket = ticketService.findTicketById(id);
        if (ticket.isEmpty())
            return ResponseEntity.notFound().build();

        ticketService.deleteTicket(id);
        return ResponseEntity.noContent().build();
    }

    // =========================
    // UPDATE — admin only
    // PUT /rest/tickets/update/{id}?requesterId=2
    // =========================
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateTicket(
            @PathVariable int id,
            @RequestParam int requesterId,
            @RequestBody Ticket updatedTicket
    ) {
        if (!isAdmin(requesterId))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        try {
            return ticketService.updateTicket(id, updatedTicket)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // =========================
    // BY USER — admin or the user themselves
    // GET /rest/tickets/user/{userId}?requesterId=5
    // =========================
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getByUser(
            @PathVariable int userId,
            @RequestParam int requesterId
    ) {
        if (!isAdminOrSelf(requesterId, userId))
            return ResponseEntity.status(403).body("Access denied: not your data");

        return ResponseEntity.ok(ticketService.findByUser(userId));
    }

    // =========================
    // BY EVENT — admin only
    // GET /rest/tickets/event/{eventId}?requesterId=2
    // =========================
    @GetMapping("/event/{eventId}")
    public ResponseEntity<?> getByEvent(
            @PathVariable int eventId,
            @RequestParam int requesterId
    ) {
        if (!isAdmin(requesterId))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        return ResponseEntity.ok(ticketService.findByEvent(eventId));
    }

    // =========================
    // CHECK SEAT — any authenticated user
    // GET /rest/tickets/seat-exists/{eventId}/{seat}?requesterId=5
    // =========================
    @GetMapping("/seat-exists/{eventId}/{seat}")
    public ResponseEntity<?> seatExists(
            @PathVariable Integer eventId,
            @PathVariable String seat,
            @RequestParam int requesterId
    ) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");

        boolean exists = ticketRepository.existsByEventIdAndSeatNumber(
                eventId,
                seat.trim().replaceAll("\\s+", "").toUpperCase()
        );

        return ResponseEntity.ok(exists);
    }
}