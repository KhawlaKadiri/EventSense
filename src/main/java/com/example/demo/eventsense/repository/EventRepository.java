package com.example.demo.eventsense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Event;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    //  search by title (partial + case insensitive)
    List<Event> findByTitleContainingIgnoreCase(String title);

    //  filter by category
    List<Event> findByCategory(String category);

    //  filter by location
    List<Event> findByLocation(String location);

    //  events before a date
    List<Event> findByEventDateBefore(LocalDateTime date);

    //  events after a date
    List<Event> findByEventDateAfter(LocalDateTime date);

    //  by organizer
    List<Event> findByOrganizerId(Integer organizerId);

    List<Event> findByAvailableSeatsGreaterThan(Integer seats);
}