package com.example.demo.eventsense.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Person;

import java.util.List;
import java.util.Optional;

@Repository
public interface PersonRepository extends JpaRepository<Person, Integer> {

    // Trouver par prénom
    List<Person> findByFirstname(String firstname);

    // Trouver par nom
    List<Person> findByLastname(String lastname);

    // Trouver par date d'anniversaire
    List<Person> findByBirthdaydate(java.time.LocalDate birthdaydate);

    // Trouver par mot de passe (pas conseillé en prod, juste pour l'exemple)
    List<Person> findByPassword(String password);

    // Trouver par prénom et nom
    List<Person> findByFirstnameAndLastname(String firstname, String lastname);


}
