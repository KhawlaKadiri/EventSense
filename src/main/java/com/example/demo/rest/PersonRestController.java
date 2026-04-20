package com.example.demo.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.example.demo.eventsense.service.PersonService;
import com.example.demo.model.Person;

import java.util.Optional;

@RestController
@RequestMapping("/rest/person")
public class PersonRestController {

    @Autowired
    private PersonService personService;

    // ===== CREATE USER =====
    @PostMapping(value = "/create", consumes = "application/json")
    public ResponseEntity<Person> createPerson(@RequestBody Person person) {

        Person saved = personService.savePerson(person);
        return ResponseEntity.ok(saved);
    }

    // ===== GET USER BY ID =====
    @GetMapping("/info/{id}")
    public ResponseEntity<Person> getPersonById(@PathVariable int id) {

        Optional<Person> personOptional = personService.findPersonById(id);

        return personOptional
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
