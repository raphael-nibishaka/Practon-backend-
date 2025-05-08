package com.unbeaten.Practon.controllers;

import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.models.Role;
import com.unbeaten.Practon.exceptions.UnauthorizedException;
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

    // Register a new user (default role is USER)
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody Person person) {
        if (personService.existsByEmail(person.getEmail())) {
            return ResponseEntity.status(401).body("Email already registered!");
        }
        person.setRole(Role.USER); // Ensure new users get USER role
        personService.addPerson(person);
        return ResponseEntity.ok().body("Successfully registered");
    }

    // Login user and return user details (excluding password)
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Person loginData) {
        Optional<Person> person = personService.findByEmail(loginData.getEmail());

        if (person.isPresent() && person.get().getPassword().equals(loginData.getPassword())) {
            Person user = person.get();
            user.setPassword("*******"); // Mask password before sending response
            return ResponseEntity.ok(user);
        }

        return ResponseEntity.status(401).body("Invalid email or password!");
    }

    // Create a new organization (only ADMIN can do this)
    @PostMapping("/create-organization")
    public ResponseEntity<?> createOrganization(@RequestBody Person organization, @RequestParam Integer adminId) {
        Person admin = personService.findPersonById(adminId);
        if (admin == null || admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only administrators can create organizations");
        }

        if (personService.existsByEmail(organization.getEmail())) {
            return ResponseEntity.status(401).body("Email already registered!");
        }

        organization.setRole(Role.ORGANIZATION);
        personService.addPerson(organization);
        return ResponseEntity.ok().body("Organization created successfully");
    }

    // Promote a user to admin (only ADMIN can do this)
    @PostMapping("/promote-to-admin/{userId}")
    public ResponseEntity<?> promoteToAdmin(@PathVariable Integer userId, @RequestParam Integer adminId) {
        Person admin = personService.findPersonById(adminId);
        if (admin == null || admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only administrators can promote users");
        }

        Person user = personService.findPersonById(userId);
        if (user == null) {
            return ResponseEntity.notFound().build();
        }

        user.setRole(Role.ADMIN);
        personService.updatePerson(user);
        return ResponseEntity.ok().body("User promoted to admin successfully");
    }

    // Get all persons (only ADMIN can do this)
    @GetMapping("/persons")
    public ResponseEntity<?> getAllPersons(@RequestParam Integer adminId) {
        Person admin = personService.findPersonById(adminId);
        if (admin == null || admin.getRole() != Role.ADMIN) {
            throw new UnauthorizedException("Only administrators can view all users");
        }
        return ResponseEntity.ok(personService.findAllPersons());
    }

    // Save new person (for internal use or testing)
    @PostMapping("/person")
    public ResponseEntity<String> savePerson(@RequestBody Person person) {
        personService.addPerson(person);
        return ResponseEntity.ok("Person saved!");
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
