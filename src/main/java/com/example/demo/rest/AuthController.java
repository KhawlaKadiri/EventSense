package com.example.demo.rest;


import com.example.demo.eventsense.dto.LoginRequest;
import com.example.demo.eventsense.dto.RegisterRequest;
import com.example.demo.eventsense.repository.UserRepository;
import com.example.demo.model.Role;
import com.example.demo.model.Users;

import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;


import java.util.Map;

@RestController
@RequestMapping("/auth")
@CrossOrigin("*")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepository;

    private final BCryptPasswordEncoder passwordEncoder =
            new BCryptPasswordEncoder();

    // ================= LOGIN =================
    @PostMapping("/login")
public ResponseEntity<?> login(@RequestBody LoginRequest req) {

    return userRepository.findByEmail(req.getEmail())

            .filter(user ->
                    passwordEncoder.matches(
                            req.getPassWord(),
                            user.getPassWord()
                    )
            )

            .<ResponseEntity<?>>map(user -> {

                user.setPassWord(null);

                return ResponseEntity.ok(user);
            })

            .orElseGet(() ->
                    ResponseEntity
                            .status(401)
                            .body("Invalid email or password")
            );
}
    // ================= REGISTER =================
    @PostMapping("/register")
    public ResponseEntity<?> register(
            @RequestBody RegisterRequest req
    ) {

        if (userRepository.findByEmail(req.getEmail()).isPresent()) {

            return ResponseEntity
                    .badRequest()
                    .body("Email already exists");
        }

        Users user = Users.builder()
                .firstname(req.getFirstname())
                .lastname(req.getLastname())
                .email(req.getEmail())
                .passWord(
                        passwordEncoder.encode(req.getPassWord())
                )
                .telephone(req.getTelephone())
                .cin(req.getCin())
                .role(Role.ROLE_USER)
                .enabled(true)
                .build();

        userRepository.save(user);

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Registration successful"
                )
        );
    }

    // ================= LOGOUT =================
    @GetMapping("/logout")
    public ResponseEntity<?> logout() {

        return ResponseEntity.ok(
                Map.of(
                        "message",
                        "Logged out successfully"
                )
        );
    }
}