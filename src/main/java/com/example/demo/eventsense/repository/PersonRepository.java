package com.example.demo.eventsense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Person;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

   

    // Trouver par prénom
    List<Person> findByFirstname(String firstname);

    // Trouver par nom
    List<Person> findByLastname(String lastname);

    // Trouver par date d'anniversaire
    List<Person> findByBirthdaydate(LocalDate birthdaydate);

    // Trouver par mot de passe
    List<Person> findByPassword(String password);

    // Trouver par prénom et nom
    List<Person> findByFirstnameAndLastname(
            String firstname,
        
            String lastname
    );

    List<Person> findByRole(String role);

    // NEW SEARCHES

    List<Person> findByPreferredCategories(String preferredCategories);

    List<Person> findByPreferredLocations(String preferredLocations);

    List<Person> findByBudgetMax(Integer budgetMax);

    List<Person> findByPreferredActors(String preferredActors);
}