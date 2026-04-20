package com.example.demo.eventsense.service;

import com.example.demo.eventsense.repository.EventRepository;
import com.example.demo.eventsense.repository.TicketRepository;
import com.example.demo.model.Event;
import com.example.demo.model.Ticket;

import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TicketService {

    private final TicketRepository ticketRepository;
    private final EventRepository eventRepository;

    public TicketService(TicketRepository ticketRepository,
                         EventRepository eventRepository) {
        this.ticketRepository = ticketRepository;
        this.eventRepository = eventRepository;
    }

    public List<Ticket> findAllTickets() {
        return ticketRepository.findAll();
    }

    public Optional<Ticket> findTicketById(int id) {
        return ticketRepository.findById(id);
    }

    public Ticket saveTicket(Ticket ticket) {

        // 🔍 check event exists
        Event event = eventRepository.findById(ticket.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // 🚫 check seats
        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        // 📅 date
        ticket.setPurchaseDate(LocalDateTime.now());

        // ✅ default status
        if (ticket.getStatus() == null) {
            ticket.setStatus("PAID");
        }

        int eventId = ticket.getEventId();
int userId = ticket.getUserId();
String seat = ticket.getSeatNumber();

int year = java.time.LocalDate.now().getYear();

ticket.setQrCode(
        "QR-" + year +
        "-EVT" + eventId +
        "-USER" + userId +
        "-" + seat
);

        // ➖ decrease seats
        event.setAvailableSeats(event.getAvailableSeats() - 1);
        eventRepository.save(event);

        return ticketRepository.save(ticket);
    }

    public void deleteTicket(int id) {
        ticketRepository.deleteById(id);
    }

    public Optional<Ticket> updateTicket(int id, Ticket updated) {
        return ticketRepository.findById(id)
                .map(t -> {

                    if (updated.getSeatNumber() != null)
                        t.setSeatNumber(updated.getSeatNumber());

                    if (updated.getQrCode() != null)
                        t.setQrCode(updated.getQrCode());

                    if (updated.getStatus() != null)
                        t.setStatus(updated.getStatus());

                    if (updated.getPricePaid() != null)
                        t.setPricePaid(updated.getPricePaid());

                    return ticketRepository.save(t);
                });
    }

    public List<Ticket> findByUser(int userId) {
        return ticketRepository.findByUserId(userId);
    }

    public List<Ticket> findByEvent(int eventId) {
        return ticketRepository.findByEventId(eventId);
    }
}