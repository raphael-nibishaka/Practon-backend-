package com.unbeaten.Practon.controllers;

import com.unbeaten.Practon.models.Address;
import com.unbeaten.Practon.models.Person;
import com.unbeaten.Practon.models.Role;
import com.unbeaten.Practon.exceptions.UnauthorizedException;
import com.unbeaten.Practon.security.JwtTokenProvider;
import com.unbeaten.Practon.services.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenProvider tokenProvider;

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

    // Login user and return JWT token
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody Person loginData) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    loginData.getEmail(),
                    loginData.getPassword()
                )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.generateToken(authentication);

            Optional<Person> personOpt = personService.findByEmail(loginData.getEmail());
            if (personOpt.isPresent()) {
                Person user = personOpt.get();
                user.setPassword("*******"); // Mask password before sending response

                Map<String, Object> response = new HashMap<>();
                response.put("token", jwt);
                response.put("user", user);

                return ResponseEntity.ok(response);
            }

            return ResponseEntity.status(401).body("Invalid email or password!");
        } catch (Exception e) {
            return ResponseEntity.status(401).body("Invalid email or password!");
        }
    }

    // Create a new organization
    @PostMapping("/create-organization")
    public ResponseEntity<?> createOrganization(@RequestBody Person organization) {
        if (personService.existsByEmail(organization.getEmail())) {
            return ResponseEntity.status(401).body("Email already registered!");
        }

        try {
            organization.setRole(Role.ORGANIZATION);
            personService.addPerson(organization);
            return ResponseEntity.ok().body("Organization created successfully");
        } catch (Exception e) {
            System.out.println("Error creating organization: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Error creating organization: " + e.getMessage());
        }
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

    // Get all persons (ADMIN and ORGANIZATION can do this)
    @GetMapping("/persons")
    public ResponseEntity<?> getAllPersons(@RequestParam(required = false) Integer adminId,
                                         @RequestParam(required = false) Integer orgId) {
        if (adminId == null && orgId == null) {
            return ResponseEntity.badRequest().body("Either adminId or orgId must be provided");
        }

        Person user = null;
        if (adminId != null) {
            user = personService.findPersonById(adminId);
            if (user != null && user.getRole() != Role.ADMIN) {
                return ResponseEntity.status(403).body("User is not an admin");
            }
        } else if (orgId != null) {
            user = personService.findPersonById(orgId);
            if (user != null && user.getRole() != Role.ORGANIZATION) {
                return ResponseEntity.status(403).body("User is not an organization");
            }
        }

        if (user == null) {
            return ResponseEntity.status(404).body("User not found");
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
