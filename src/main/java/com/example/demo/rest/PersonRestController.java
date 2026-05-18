package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.eventsense.service.PersonService;
import com.example.demo.model.Person;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/rest/person")
@CrossOrigin(origins = "*")
public class PersonRestController {

    @Autowired
    private PersonService personService;

    // ===== CREATE USER =====
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {

        Person saved = personService.savePerson(person);

        return ResponseEntity.ok(saved);
    }

    // ===== GET ALL USERS =====
    @GetMapping("/all")
    public ResponseEntity<List<Person>> getAllPersons() {

        return ResponseEntity.ok(personService.findAllPersons());
    }

    // ===== GET USER BY ID =====
    @GetMapping("/info/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable int id) {

        Optional<Person> personOptional =
                personService.findPersonById(id);

        return personOptional
                .map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.notFound().build()
                );
    }

    // ===== DELETE USER =====
    @DeleteMapping("/delete/{id}")
public ResponseEntity<?> deletePerson(@PathVariable int id) {

    try {

        Optional<Person> person = personService.findPersonById(id);

        if (person.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body("User not found");
        }

        personService.deletePerson(id);

        return ResponseEntity.ok("User deleted");

    } catch (Exception e) {

        return ResponseEntity
                .status(500)
                .body("ERROR: " + e.getMessage());
    }
}

    // ===== UPDATE USER =====
    @PutMapping(value = "/update/{id}",
            consumes = "application/json")
    public ResponseEntity<Person> updatePerson(
            @PathVariable int id,
            @RequestBody Person person) {

        Optional<Person> existingPerson =
                personService.findPersonById(id);

        if (existingPerson.isEmpty()) {

            return ResponseEntity.notFound().build();
        }

        Person p = existingPerson.get();

        // BASIC FIELDS
        p.setFirstname(person.getFirstname());

        p.setLastname(person.getLastname());

        p.setBirthdaydate(person.getBirthdaydate());

        p.setPassword(person.getPassword());

        // NEW PREFERENCE FIELDS
        p.setPreferredCategories(
                person.getPreferredCategories()
        );

        p.setPreferredLocations(
                person.getPreferredLocations()
        );

        p.setBudgetMax(
                person.getBudgetMax()
        );

        p.setPreferredActors(
                person.getPreferredActors()
        );

        p.setPreferenceEmbedding(
                person.getPreferenceEmbedding()
        );

        Person updated = personService.savePerson(p);

        return ResponseEntity.ok(updated);
    }

    // ===== SEARCH BY FIRSTNAME =====
    @GetMapping("/firstname/{firstname}")
    public ResponseEntity<List<Person>> findByFirstname(
            @PathVariable String firstname) {

        return ResponseEntity.ok(
                personService.findByFirstname(firstname)
        );
    }

    // ===== SEARCH BY LASTNAME =====
    @GetMapping("/lastname/{lastname}")
    public ResponseEntity<List<Person>> findByLastname(
            @PathVariable String lastname) {

        return ResponseEntity.ok(
                personService.findByLastname(lastname)
        );
    }

    // ===== SEARCH BY CATEGORY =====
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Person>> findByCategory(
            @PathVariable String category) {

        return ResponseEntity.ok(
                personService.findByPreferredCategories(category)
        );
    }

    // ===== SEARCH BY LOCATION =====
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Person>> findByLocation(
            @PathVariable String location) {

        return ResponseEntity.ok(
                personService.findByPreferredLocations(location)
        );
    }
}