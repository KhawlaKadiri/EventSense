package com.example.demo.eventsense.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.eventsense.repository.PersonRepository;
import com.example.demo.model.Person;

import java.util.List;
import java.util.Optional;

@Service
public class PersonService {

    @Autowired
    private PersonRepository personRepository;

    // Lire tous les utilisateurs
    public List<Person> findAllPersons() {
        return personRepository.findAll();
    }

    // Lire un utilisateur par ID
    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    // Créer ou mettre à jour un utilisateur
    public Person savePerson(Person person) {
        return personRepository.save(person);
    }

    // Supprimer un utilisateur par ID
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    // Mettre à jour un utilisateur (optionnel)
    public Optional<Person> updatePerson(int id, Person updatedPerson) {
        return personRepository.findById(id)
            .map(existingPerson -> {
                existingPerson.setFirstname(updatedPerson.getFirstname());
                existingPerson.setLastname(updatedPerson.getLastname());
                existingPerson.setBirthdaydate(updatedPerson.getBirthdaydate());
                existingPerson.setPassword(updatedPerson.getPassword());
                return personRepository.save(existingPerson);
            });
    }
}
