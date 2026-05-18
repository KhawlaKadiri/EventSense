package com.example.demo.model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "users")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String firstname;

    private String lastname;

    private LocalDate birthdaydate;

    @Column(name = "pass_word")
    private String password;

    // NEW FIELDS

    @Column(name = "preferred_categories")
    private String preferredCategories;

    @Column(name = "preferred_locations")
    private String preferredLocations;

    @Column(name = "budget_max")
    private Integer budgetMax;

    @Column(name = "preferred_actors")
    private String preferredActors;

    @Column(name = "preference_embedding")
    private String preferenceEmbedding;

    // CONSTRUCTORS

    public Person() {
    }

    public Person(String firstname,
                  String lastname,
                  LocalDate birthdaydate,
                  String password,
                  String preferredCategories,
                  String preferredLocations,
                  Integer budgetMax,
                  String preferredActors,
                  String preferenceEmbedding) {

        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdaydate = birthdaydate;
        this.password = password;
        this.preferredCategories = preferredCategories;
        this.preferredLocations = preferredLocations;
        this.budgetMax = budgetMax;
        this.preferredActors = preferredActors;
        this.preferenceEmbedding = preferenceEmbedding;
    }

    // GETTERS & SETTERS

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public LocalDate getBirthdaydate() {
        return birthdaydate;
    }

    public void setBirthdaydate(LocalDate birthdaydate) {
        this.birthdaydate = birthdaydate;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPreferredCategories() {
        return preferredCategories;
    }

    public void setPreferredCategories(String preferredCategories) {
        this.preferredCategories = preferredCategories;
    }

    public String getPreferredLocations() {
        return preferredLocations;
    }

    public void setPreferredLocations(String preferredLocations) {
        this.preferredLocations = preferredLocations;
    }

    public Integer getBudgetMax() {
        return budgetMax;
    }

    public void setBudgetMax(Integer budgetMax) {
        this.budgetMax = budgetMax;
    }

    public String getPreferredActors() {
        return preferredActors;
    }

    public void setPreferredActors(String preferredActors) {
        this.preferredActors = preferredActors;
    }

    public String getPreferenceEmbedding() {
        return preferenceEmbedding;
    }

    
    public void setPreferenceEmbedding(String preferenceEmbedding) {
        this.preferenceEmbedding = preferenceEmbedding;
    }
}