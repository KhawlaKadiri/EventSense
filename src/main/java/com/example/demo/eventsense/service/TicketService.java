
package com.example.demo.eventsense.service;

import com.example.demo.eventsense.repository.EventRepository;
import com.example.demo.eventsense.repository.TicketRepository;
import com.example.demo.model.Event;
import com.example.demo.model.Ticket;

import org.springframework.stereotype.Service;

import java.math.BigDecimal;
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

        // =========================
        // EVENT CHECK
        // =========================
        Event event = eventRepository.findById(ticket.getEventId())
                .orElseThrow(() -> new RuntimeException("Event not found"));

        // =========================
        // AVAILABLE SEATS CHECK
        // =========================
        if (event.getAvailableSeats() <= 0) {
            throw new RuntimeException("No seats available");
        }

        // =========================
        // SEAT CHECK
        // =========================
        String seat = ticket.getSeatNumber();

        if (seat == null || seat.trim().isEmpty()) {
            throw new RuntimeException("Seat required");
        }

        String safeSeat = seat.trim()
                .replaceAll("\\s+", "")
                .toUpperCase();

        boolean seatExists =
                ticketRepository.existsByEventIdAndSeatNumber(
                        ticket.getEventId(),
                        safeSeat
                );

        if (seatExists) {
            throw new RuntimeException("Seat already taken");
        }

        ticket.setSeatNumber(safeSeat);

        // =========================
        // PRICE CHECK
        // =========================
        BigDecimal realPrice = event.getPrice();
        BigDecimal paid = ticket.getPricePaid();

        if (paid == null) {
            paid = BigDecimal.ZERO;
            ticket.setPricePaid(paid);
        }

        // ❌ USER CAN NOT PAY MORE
        if (paid.compareTo(realPrice) > 0) {
            throw new RuntimeException(
                    "Price can not exceed real ticket price"
            );
        }

        // ✅ STATUS
        if (paid.compareTo(realPrice) == 0) {
            ticket.setStatus("PAID");
        } else {
            ticket.setStatus("NOT_FULLY_PAID");
        }

        // =========================
        // DATE
        // =========================
        ticket.setPurchaseDate(LocalDateTime.now());

        // =========================
        // QR CODE
        // =========================
        int eventId = ticket.getEventId();
        int userId = ticket.getUserId();

        int year = java.time.LocalDate.now().getYear();

        String userName;

        if (ticket.getUserName() != null &&
                !ticket.getUserName().trim().isEmpty()) {

            userName = ticket.getUserName()
                    .trim()
                    .replaceAll("\\s+", "")
                    .toUpperCase();

        } else {
            userName = "USER" + userId;
        }

        ticket.setQrCode(
                "QR-" + year +
                "-EVT" + eventId +
                "-USER" + userName +
                "-" + safeSeat
        );

        // =========================
        // DECREASE SEATS
        // =========================
        event.setAvailableSeats(
                event.getAvailableSeats() - 1
        );

        eventRepository.save(event);

        // =========================
        // SAVE
        // =========================
        return ticketRepository.save(ticket);
    }

    public void deleteTicket(int id) {
        ticketRepository.deleteById(id);
    }

    public Optional<Ticket> updateTicket(int id, Ticket updated) {

        return ticketRepository.findById(id)
                .map(t -> {

                    // =========================
                    // UPDATE SEAT
                    // =========================
                    if (updated.getSeatNumber() != null) {

                        String newSeat = updated.getSeatNumber()
                                .trim()
                                .replaceAll("\\s+", "")
                                .toUpperCase();

                        boolean seatExists =
                                ticketRepository
                                .existsByEventIdAndSeatNumber(
                                        t.getEventId(),
                                        newSeat
                                );

                        // allow same seat for same ticket
                        if (seatExists &&
                                !newSeat.equals(t.getSeatNumber())) {

                            throw new RuntimeException(
                                    "Seat already taken"
                            );
                        }

                        t.setSeatNumber(newSeat);
                    }

                    // =========================
                    // UPDATE PRICE
                    // =========================
                    if (updated.getPricePaid() != null) {

                        Event event = eventRepository
                                .findById(t.getEventId())
                                .orElseThrow();

                        BigDecimal realPrice = event.getPrice();
                        BigDecimal paid = updated.getPricePaid();

                        if (paid.compareTo(realPrice) > 0) {
                            throw new RuntimeException(
                                    "Price exceeds real ticket price"
                            );
                        }

                        t.setPricePaid(paid);

                        if (paid.compareTo(realPrice) == 0) {
                            t.setStatus("PAID");
                        } else {
                            t.setStatus("NOT_FULLY_PAID");
                        }
                    }

                    // =========================
                    // QR CODE
                    // =========================
                    if (updated.getQrCode() != null)
                        t.setQrCode(updated.getQrCode());

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