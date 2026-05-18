package com.example.demo.eventsense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.demo.model.Ticket;

import java.util.List;

@Repository
public interface TicketRepository extends JpaRepository<Ticket, Integer> {

    List<Ticket> findByUserId(Integer userId);

    List<Ticket> findByEventId(Integer eventId);

    List<Ticket> findByStatus(String status);

    boolean existsByEventIdAndSeatNumber(Integer eventId, String seatNumber);
}