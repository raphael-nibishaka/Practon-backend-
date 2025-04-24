package com.unbeaten.Practon.controllers;

import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@RestController
@CrossOrigin(origins = "http://localhost:3000") // Update to match your React frontend port
@RequestMapping("/")
public class PersonController {

    @Autowired
    private PersonService personService;
    private Map<String, String> passwordResetTokens = new HashMap<>();

    @GetMapping("/hello")
    public String hello() {
        return "Hello world";
    }

    // ✅ Register a new user
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Person person) {
        if (personService.existsByEmail(person.getEmail())) {
            return ResponseEntity.status(401).body("Email already registered!");
        }
        personService.addPerson(person);
        return ResponseEntity.ok().body("Successfully registered");
    }

    // ✅ Login user and return user details (excluding password)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Person loginData) {
        Optional<Person> person = personService.findByEmail(loginData.getEmail());

        if (person.isPresent() && person.get().getPassword().equals(loginData.getPassword())) {
            Person user = person.get();
            user.setPassword("********"); // Mask password before sending response
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(401).body("Invalid email or password!");
    }

    // ✅ Save new person (for internal use or testing)
    @PostMapping("/person")
    public ResponseEntity<String> savePerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.ok("Person saved!");
    }

    // ✅ Get all persons
    @GetMapping("/persons")
    public ResponseEntity<List<Person>> getAllPersons() {
        return ResponseEntity.ok(personService.findAllPersons());
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        Optional<Person> person = personService.findByEmail(email);

        if (person.isPresent()) {
            // Generate a unique reset token
            String resetToken = UUID.randomUUID().toString();

            // Store the token with the user's email (in a real app, save to database with
            // expiration)
            passwordResetTokens.put(resetToken, email);

            // In a real application, send an email with the reset link
            // For now, we'll just return the token in the response
            Map<String, String> response = new HashMap<>();
            response.put("message", "Password reset link has been sent to your email");
            response.put("token", resetToken); // In production, don't return the token directly

            return ResponseEntity.ok(response);
        } else {
            return ResponseEntity.status(404).body("Email not found");
        }
    }

    // ✅ Reset password using token
    @PostMapping("/reset-password")
    public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("password");

        // Validate token
        String email = passwordResetTokens.get(token);

        if (email == null) {
            return ResponseEntity.badRequest().body("Invalid or expired reset token");
        }

        // Find user and update password
        Optional<Person> personOpt = personService.findByEmail(email);
        if (personOpt.isPresent()) {
            Person person = personOpt.get();
            person.setPassword(newPassword);
            personService.updatePerson(person);

            // Remove used token
            passwordResetTokens.remove(token);

            return ResponseEntity.ok("Password has been reset successfully");
        } else {
            return ResponseEntity.status(404).body("User not found");
        }
    }

    // ✅ Validate reset token
    @GetMapping("/validate-reset-token/{token}")
    public ResponseEntity<?> validateResetToken(@PathVariable String token) {
        String email = passwordResetTokens.get(token);

        if (email != null) {
            return ResponseEntity.ok("Valid token");
        } else {
            return ResponseEntity.badRequest().body("Invalid or expired token");
        }
    }
}
