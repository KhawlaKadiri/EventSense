package com.example.demo.eventsense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Event;

import java.time.LocalDateTime;
import java.util.List;


@Repository
public interface EventRepository extends JpaRepository<Event, Integer> {

    // Trouver les événements par titre (exact ou partiel)
    List<Event> findByTitleContainingIgnoreCase(String title);

    // Trouver les événements par catégorie
    List<Event> findByCategory(String category);

    // Trouver les événements par lieu (location)
    List<Event> findByLocation(String location);

    // Trouver les événements avant une date donnée (utile pour événements futurs)
    List<Event> findByEventDateBefore(LocalDateTime date);

    // Trouver les événements après une date donnée (événements à venir)
    List<Event> findByEventDateAfter(LocalDateTime date);

    // Trouver les événements par organisateur
    List<Event> findByOrganizerId(Integer organizerId);

    com.example.demo.model.Event save(com.example.demo.model.Event event);

    // Autres méthodes personnalisées peuvent être ajoutées ici selon besoin
}
