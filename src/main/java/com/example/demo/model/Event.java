package com.example.demo.model;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, length = 255)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(length = 100)
    private String category;

    @Column(length = 255)
    private String location;

    @Column(name = "event_date", nullable = false)
    private LocalDateTime eventDate;

    @Column(name = "duration_minutes")
    private Integer durationMinutes;

    @Column(precision = 8, scale = 2)
    private BigDecimal price;

    // ✅ NEW FIELD: budget
    @Column(precision = 10, scale = 2)
    private BigDecimal budget;

    @Column(name = "age_min")
    private Integer ageMin;

    @Column(name = "age_max")
    private Integer ageMax;

    @Column(name = "total_seats")
    private Integer totalSeats;

    @Column(name = "available_seats")
    private Integer availableSeats;

    @Column(name = "organizer_id")
    private Integer organizerId;

    // ✅ NEW FIELD: actors
    @Column(columnDefinition = "TEXT")
    private String actors;

    // ✅ NEW FIELD: embedding
    @Column(columnDefinition = "TEXT")
    private String embedding;

    @Column(name = "created_at", insertable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", insertable = false, updatable = false)
    private LocalDateTime updatedAt;

   

    public Event() {}

    // ===== GETTERS & SETTERS =====


    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public LocalDateTime getEventDate() { return eventDate; }
    public void setEventDate(LocalDateTime eventDate) { this.eventDate = eventDate; }

    public Integer getDurationMinutes() { return durationMinutes; }
    public void setDurationMinutes(Integer durationMinutes) { this.durationMinutes = durationMinutes; }

    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }

    // ✅ NEW GETTER/SETTER
    public BigDecimal getBudget() { return budget; }
    public void setBudget(BigDecimal budget) { this.budget = budget; }

    public Integer getAgeMin() { return ageMin; }
    public void setAgeMin(Integer ageMin) { this.ageMin = ageMin; }

    public Integer getAgeMax() { return ageMax; }
    public void setAgeMax(Integer ageMax) { this.ageMax = ageMax; }

    public Integer getTotalSeats() { return totalSeats; }
    public void setTotalSeats(Integer totalSeats) { this.totalSeats = totalSeats; }

    public Integer getAvailableSeats() { return availableSeats; }
    public void setAvailableSeats(Integer availableSeats) { this.availableSeats = availableSeats; }

    public Integer getOrganizerId() { return organizerId; }
    public void setOrganizerId(Integer organizerId) { this.organizerId = organizerId; }

    // ✅ NEW GETTERS/SETTERS
    public String getActors() { return actors; }
    public void setActors(String actors) { this.actors = actors; }

    public String getEmbedding() { return embedding; }
    public void setEmbedding(String embedding) { this.embedding = embedding; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }
}