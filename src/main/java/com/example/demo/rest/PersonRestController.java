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

    // ================= ADMIN ONLY =================

    // GET ALL USERS (ADMIN ONLY)
    @GetMapping("/users")
    public ResponseEntity<?> getUsers(@RequestParam int requesterId) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        return ResponseEntity.ok(personService.findUsersOnly());
    }

    // DELETE USER (ADMIN ONLY)
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> delete(@RequestParam int requesterId,
                                    @PathVariable int id) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        if (personService.findPersonById(id).isEmpty())
            return ResponseEntity.status(404).body("User not found");

        personService.deletePerson(id);
        return ResponseEntity.ok("User deleted");
    }

       @PostMapping("/create")
    public ResponseEntity<?> createUser(
            @RequestParam int requesterId,
            @RequestBody Person person
    ) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        try {
            Person saved = personService.savePerson(person);
            return ResponseEntity.status(201).body(saved);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

@PostMapping("/auth/register")
public ResponseEntity<?> register(@RequestBody Person person) {
    try {
        // Normalize role before saving
        String role = person.getRole();
        if (role == null || role.isEmpty() || "USER".equals(role)) {
            person.setRole("ROLE_USER");
        } else if ("ADMIN".equals(role)) {
            person.setRole("ROLE_ADMIN");
        }
        
        Person saved = personService.savePerson(person);
        return ResponseEntity.status(201).body(saved);
    } catch (RuntimeException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

      // UPDATE USER (ADMIN ONLY)
    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateUser(
            @PathVariable int id,
            @RequestParam int requesterId,
            @RequestBody Person updatedPerson
    ) {
        Optional<Person> requester = personService.findPersonById(requesterId);
        if (requester.isEmpty())
            return ResponseEntity.status(404).body("Requester not found");
        if (!"ROLE_ADMIN".equals(requester.get().getRole()))
            return ResponseEntity.status(403).body("Access denied: Admins only");

        try {
            return personService.updatePerson(id, updatedPerson)
                    .map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // ================= PUBLIC =================

    // GET BY ID
    @GetMapping("/info/{id}")
    public ResponseEntity<Person> getById(@PathVariable int id) {
        return personService.findPersonById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // ================= SEARCH =================

    @GetMapping("/firstname/{firstname}")
    public List<Person> byFirstname(@PathVariable String firstname) {
        return personService.findByFirstname(firstname);
    }

    @GetMapping("/lastname/{lastname}")
    public List<Person> byLastname(@PathVariable String lastname) {
        return personService.findByLastname(lastname);
    }

    @GetMapping("/category/{category}")
    public List<Person> byCategory(@PathVariable String category) {
        return personService.findByPreferredCategories(category);
    }

    @GetMapping("/location/{location}")
    public List<Person> byLocation(@PathVariable String location) {
        return personService.findByPreferredLocations(location);
    }
}