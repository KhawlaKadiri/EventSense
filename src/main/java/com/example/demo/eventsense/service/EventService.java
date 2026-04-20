package com.example.demo.eventsense.service;

import com.example.demo.eventsense.repository.EventRepository;
import com.example.demo.model.Event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    // Lire tous les événements
    public List<Event> findAllEvents() {
        return eventRepository.findAll();
    }

    // Lire un événement par ID
    public Optional<Event> findEventById(int id) {
        return eventRepository.findById(id);
    }

    // Créer ou mettre à jour un événement
    public Event saveEvent(Event event) {
        return eventRepository.save(event);
    }

    // Supprimer un événement par ID
    public void deleteEvent(int id) {
        eventRepository.deleteById(id);
    }

    // Mettre à jour un événement (optionnel)
    public Optional<Event> updateEvent(int id, Event updatedEvent) {
        return eventRepository.findById(id)
            .map(existingEvent -> {
                existingEvent.setTitle(updatedEvent.getTitle());
                existingEvent.setDescription(updatedEvent.getDescription());
                existingEvent.setCategory(updatedEvent.getCategory());
                existingEvent.setLocation(updatedEvent.getLocation());
                existingEvent.setEventDate(updatedEvent.getEventDate());
                existingEvent.setDurationMinutes(updatedEvent.getDurationMinutes());
                existingEvent.setPrice(updatedEvent.getPrice());
                existingEvent.setAgeMin(updatedEvent.getAgeMin());
                existingEvent.setAgeMax(updatedEvent.getAgeMax());
                existingEvent.setTotalSeats(updatedEvent.getTotalSeats());
                existingEvent.setAvailableSeats(updatedEvent.getAvailableSeats());
                existingEvent.setOrganizerId(updatedEvent.getOrganizerId());
                return eventRepository.save(existingEvent);
            });
    }
    public List<Event> findByTitle(String title) {
    return eventRepository.findByTitleContainingIgnoreCase(title);
}

public List<Event> findByCategory(String category) {
    return eventRepository.findByCategory(category);
}

public List<Event> findByLocation(String location) {
    return eventRepository.findByLocation(location);
}
}
