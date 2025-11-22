package com.foodapp.controller;

import com.foodapp.model.User;
import com.foodapp.service.SessionService;
import com.foodapp.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody User registrationDetails) {
        try {
            System.out.println("Registration attempt for: " + registrationDetails.getEmail());
            User newUser = userService.registerUser(registrationDetails);

            if (newUser != null) {
                return new ResponseEntity<>(newUser, HttpStatus.CREATED);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Registration failed. Email already exists.");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            System.out.println("Registration error: " + e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Registration failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestBody LoginRequest loginRequest) {
        try {
            System.out.println("Login attempt for: " + loginRequest.getEmail());
            String sessionToken = userService.loginUser(loginRequest.getEmail(), loginRequest.getPassword());

            if (sessionToken != null) {
                Map<String, Object> response = new HashMap<>();
                response.put("message", "Login successful");
                response.put("sessionToken", sessionToken);
                response.put("timestamp", java.time.LocalDateTime.now());
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid email or password");
                return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
            }
        } catch (Exception e) {
            System.out.println("Login error: " + e.getMessage());
            Map<String, String> response = new HashMap<>();
            response.put("error", "Login failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logoutUser(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String sessionToken = extractSessionToken(authHeader);

            if (sessionToken != null && userService.logoutUser(sessionToken)) {
                Map<String, String> response = new HashMap<>();
                response.put("message", "Logout successful");
                return new ResponseEntity<>(response, HttpStatus.OK);
            } else {
                Map<String, String> response = new HashMap<>();
                response.put("error", "Invalid session token");
                return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Logout failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/profile")
    public ResponseEntity<?> updateProfile(@RequestBody User updatedDetails,
                                           @RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String sessionToken = extractSessionToken(authHeader);
            String userId = SessionService.getUserIdFromToken(sessionToken);

            if (userId != null && SessionService.validateSession(sessionToken)) {
                User updatedUser = userService.updateUserProfile(userId, updatedDetails);
                if (updatedUser != null) {
                    return new ResponseEntity<>(updatedUser, HttpStatus.OK);
                }
            }

            Map<String, String> response = new HashMap<>();
            response.put("error", "Unable to update profile. Invalid session.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Profile update failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/profile")
    public ResponseEntity<?> getProfile(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        try {
            String sessionToken = extractSessionToken(authHeader);
            String userId = SessionService.getUserIdFromToken(sessionToken);

            if (userId != null && SessionService.validateSession(sessionToken)) {
                User user = userService.getUserById(userId);
                if (user != null) {
                    return new ResponseEntity<>(user, HttpStatus.OK);
                }
            }

            Map<String, String> response = new HashMap<>();
            response.put("error", "Unable to fetch profile. Invalid session.");
            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);

        } catch (Exception e) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Profile fetch failed: " + e.getMessage());
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private String extractSessionToken(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return authHeader;
    }

    // DTO for login request
    public static class LoginRequest {
        private String email;
        private String password;

        public String getEmail() { return email; }
        public void setEmail(String email) { this.email = email; }
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
    }
}