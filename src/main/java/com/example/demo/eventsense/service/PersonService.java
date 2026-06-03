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

    // READ user only
   
    public List<Person> findUsersOnly() {
    return personRepository.findByRole("ROLE_USER");
}

    // READ BY ID
    public Optional<Person> findPersonById(int id) {
        return personRepository.findById(id);
    }

    // CREATE / SAVE (DEFAULT ROLE ADDED HERE)
    public Person savePerson(Person person) {

        // ✅ IMPORTANT: default role if null
        if (person.getRole() == null || person.getRole().isEmpty()) {
            person.setRole("ROLE_USER");
        }

        return personRepository.save(person);
    }

    // DELETE
    public void deletePerson(int id) {
        personRepository.deleteById(id);
    }

    // UPDATE
    public Optional<Person> updatePerson(int id, Person updatedPerson) {

        return personRepository.findById(id)
            .map(existingPerson -> {

                existingPerson.setFirstname(updatedPerson.getFirstname());
                existingPerson.setLastname(updatedPerson.getLastname());
                existingPerson.setBirthdaydate(updatedPerson.getBirthdaydate());
                existingPerson.setPassword(updatedPerson.getPassword());

                // ROLE UPDATE (ONLY ADMIN SHOULD USE THIS ENDPOINT)
                if (updatedPerson.getRole() != null) {
                    existingPerson.setRole(updatedPerson.getRole());
                }

                // NEW FIELDS
                existingPerson.setPreferredCategories(updatedPerson.getPreferredCategories());
                existingPerson.setPreferredLocations(updatedPerson.getPreferredLocations());
                existingPerson.setBudgetMax(updatedPerson.getBudgetMax());
                existingPerson.setPreferredActors(updatedPerson.getPreferredActors());
                

                return personRepository.save(existingPerson);
            });
    }

    // SEARCH
    public List<Person> findByFirstname(String firstname) {
        return personRepository.findByFirstname(firstname);
    }

    public List<Person> findByLastname(String lastname) {
        return personRepository.findByLastname(lastname);
    }

    public List<Person> findByPreferredCategories(String category) {
        return personRepository.findByPreferredCategories(category);
    }

    public List<Person> findByPreferredLocations(String location) {
        return personRepository.findByPreferredLocations(location);
    }

    // 🔐 OPTIONAL: promote user to admin (for testing)
    public Person promoteToAdmin(int id) {
        Person p = personRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));

        p.setRole("ROLE_ADMIN");
        return personRepository.save(p);
    }
}