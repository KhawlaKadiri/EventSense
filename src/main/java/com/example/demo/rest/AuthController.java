package com.example.demo.rest;

import com.example.demo.eventsense.dto.LoginRequest;
import com.example.demo.eventsense.dto.RegisterRequest;
import com.example.demo.eventsense.repository.UserRepository;
import com.example.demo.model.Users;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    // ================= LOGIN =================
  @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest req) {
    try {
        return userRepository.findByEmail(req.getEmail())
                .map(user -> {
                    String storedPassword = user.getPassword();
                    
                    if (storedPassword == null || storedPassword.isEmpty()) {
                        return ResponseEntity.status(401).body("Password not set");
                    }
                    
                    boolean matches;
                    if (storedPassword.startsWith("$2a$")) {
                        matches = passwordEncoder.matches(req.getPassWord(), storedPassword);
                    } else {
                        matches = storedPassword.equals(req.getPassWord());
                    }

                    if (!matches) {
                        return ResponseEntity.status(401).body("Invalid password");
                    }

                    // Build safe response - NEVER return the entity directly
                    Map<String, Object> safeUser = new HashMap<>();
                    safeUser.put("id", user.getId());
                    safeUser.put("firstname", user.getFirstname());
                    safeUser.put("lastname", user.getLastname());
                    safeUser.put("email", user.getEmail());
                    safeUser.put("cin", user.getCin());
                    safeUser.put("telephone", user.getTelephone());
                    
                    safeUser.put("role", user.getRole());
                    safeUser.put("preferredCategories", user.getPreferredCategories());
                    safeUser.put("preferredLocations", user.getPreferredLocations());
                    safeUser.put("budgetMax", user.getBudgetMax());
                    safeUser.put("preferredActors", user.getPreferredActors());
                    safeUser.put("age", user.getAge());
                    safeUser.put("birthdaydate", user.getBirthdaydate());

                    if ("ROLE_ADMIN".equals(user.getRole())) {
                        return ResponseEntity.ok(Map.of(
                            "message", "Admin login successful",
                            "role", "ADMIN",
                            "user", safeUser,
                            "redirect", "/admin-dashboard.html"
                        ));
                    }

                    return ResponseEntity.ok(Map.of(
                        "message", "User login successful",
                        "role", "USER",
                        "user", safeUser,
                        "redirect", "/user.html"
                    ));
                })
                .orElse(ResponseEntity.status(401).body("User not found"));
    } catch (Exception e) {
        e.printStackTrace();
        return ResponseEntity.status(500).body("Server error: " + e.getMessage());
    }
}

@GetMapping("/encode/{password}")
public String encode(@PathVariable String password) {
    return passwordEncoder.encode(password);
}
    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {
            return ResponseEntity.badRequest()
                    .body("Email already exists");
        }

        Users user = new Users();
        user.setFirstname(req.getFirstname());
        user.setLastname(req.getLastname());
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassWord()));
        user.setTelephone(req.getTelephone());
        user.setCin(req.getCin());
        user.setRole("ROLE_USER");

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of("message", "Registration successful")
        );
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.ok(
                Map.of("message", "Logged out successfully")
        );
    }
}