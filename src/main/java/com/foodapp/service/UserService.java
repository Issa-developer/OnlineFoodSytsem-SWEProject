package com.foodapp.service;

import com.foodapp.model.User;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class UserService {
    // In-memory storage
    private final Map<String, User> userDatabase = new ConcurrentHashMap<>();

    public User registerUser(User registrationDetails) {
        System.out.println("=== REGISTRATION ATTEMPT ===");
        System.out.println("Username: " + registrationDetails.getUsername());
        System.out.println("Email: " + registrationDetails.getEmail());
        System.out.println("Password received: " + registrationDetails.getPassword());

        // Check if user already exists
        if (userDatabase.values().stream()
                .anyMatch(user -> user.getEmail().equals(registrationDetails.getEmail()))) {
            System.out.println("REGISTRATION FAILED: User already exists");
            return null;
        }

        // Create new user object and COPY ALL FIELDS
        String userId = UUID.randomUUID().toString();
        User newUser = new User();
        newUser.setUserId(userId);
        newUser.setUsername(registrationDetails.getUsername());
        newUser.setEmail(registrationDetails.getEmail());
        newUser.setPassword(registrationDetails.getPassword()); // COPY PASSWORD
        newUser.setPhoneNumber(registrationDetails.getPhoneNumber());
        newUser.setAddress(registrationDetails.getAddress());

        userDatabase.put(userId, newUser);

        System.out.println("REGISTRATION SUCCESS:");
        System.out.println("User ID: " + newUser.getUserId());
        System.out.println("Email: " + newUser.getEmail());
        System.out.println("Password saved: " + newUser.getPassword());
        System.out.println("Total users in database: " + userDatabase.size());

        // Return the user WITHOUT password for security
        User responseUser = new User();
        responseUser.setUserId(newUser.getUserId());
        responseUser.setUsername(newUser.getUsername());
        responseUser.setEmail(newUser.getEmail());
        responseUser.setPhoneNumber(newUser.getPhoneNumber());
        responseUser.setAddress(newUser.getAddress());
        responseUser.setCreatedAt(newUser.getCreatedAt());
        responseUser.setActive(newUser.isActive());

        return responseUser;
    }

    public String loginUser(String email, String password) {
        System.out.println("=== LOGIN ATTEMPT ===");
        System.out.println("Email: " + email);
        System.out.println("Password attempt: " + password);

        // Find user by email
        Optional<User> userOptional = userDatabase.values().stream()
                .filter(user -> user.getEmail().equals(email) && user.isActive())
                .findFirst();

        if (userOptional.isPresent()) {
            User user = userOptional.get();
            System.out.println("USER FOUND:");
            System.out.println("User ID: " + user.getUserId());
            System.out.println("Email: " + user.getEmail());
            System.out.println("Stored password: " + user.getPassword());
            System.out.println("Active status: " + user.isActive());

            // Simple password comparison
            if (password.equals(user.getPassword())) {
                System.out.println("✅ PASSWORD MATCHES!");
                // Update last login
                user.setLastLoginAt(java.time.LocalDateTime.now());

                // Create session
                String sessionToken = SessionService.createSession(user.getUserId());
                System.out.println("🎉 LOGIN SUCCESSFUL");
                System.out.println("Session token: " + sessionToken);
                return sessionToken;
            } else {
                System.out.println("❌ PASSWORD DOES NOT MATCH!");
                System.out.println("Expected: " + user.getPassword());
                System.out.println("Received: " + password);
            }
        } else {
            System.out.println("❌ USER NOT FOUND!");
            System.out.println("Available users in database:");
            userDatabase.values().forEach(u -> System.out.println(" - " + u.getEmail()));
        }
        System.out.println("🚫 LOGIN FAILED");
        return null;
    }

    public boolean logoutUser(String sessionToken) {
        System.out.println("=== LOGOUT ATTEMPT ===");
        System.out.println("Session token: " + sessionToken);
        boolean result = SessionService.invalidateSession(sessionToken);
        System.out.println("Logout result: " + (result ? "SUCCESS" : "FAILED"));
        return result;
    }

    public User updateUserProfile(String userId, User updatedDetails) {
        System.out.println("=== PROFILE UPDATE ===");
        System.out.println("User ID: " + userId);

        User user = userDatabase.get(userId);

        if (user != null) {
            System.out.println("User found, updating fields...");

            if (updatedDetails.getUsername() != null) {
                System.out.println("Updating username: " + user.getUsername() + " → " + updatedDetails.getUsername());
                user.setUsername(updatedDetails.getUsername());
            }
            if (updatedDetails.getPhoneNumber() != null) {
                System.out.println("Updating phone: " + user.getPhoneNumber() + " → " + updatedDetails.getPhoneNumber());
                user.setPhoneNumber(updatedDetails.getPhoneNumber());
            }
            if (updatedDetails.getAddress() != null) {
                System.out.println("Updating address: " + user.getAddress() + " → " + updatedDetails.getAddress());
                user.setAddress(updatedDetails.getAddress());
            }

            userDatabase.put(userId, user);

            // Return user without password
            User responseUser = new User();
            responseUser.setUserId(user.getUserId());
            responseUser.setUsername(user.getUsername());
            responseUser.setEmail(user.getEmail());
            responseUser.setPhoneNumber(user.getPhoneNumber());
            responseUser.setAddress(user.getAddress());
            responseUser.setCreatedAt(user.getCreatedAt());
            responseUser.setActive(user.isActive());

            System.out.println("Profile update SUCCESS");
            return responseUser;
        }

        System.out.println("Profile update FAILED - User not found");
        return null;
    }

    public User getUserById(String userId) {
        System.out.println("=== GET USER BY ID ===");
        System.out.println("User ID: " + userId);

        User user = userDatabase.get(userId);
        if (user != null) {
            System.out.println("User found: " + user.getEmail());

            // Return user without password
            User responseUser = new User();
            responseUser.setUserId(user.getUserId());
            responseUser.setUsername(user.getUsername());
            responseUser.setEmail(user.getEmail());
            responseUser.setPhoneNumber(user.getPhoneNumber());
            responseUser.setAddress(user.getAddress());
            responseUser.setCreatedAt(user.getCreatedAt());
            responseUser.setActive(user.isActive());
            return responseUser;
        }

        System.out.println("User not found");
        return null;
    }

    public boolean deactivateUser(String userId) {
        System.out.println("=== DEACTIVATE USER ===");
        System.out.println("User ID: " + userId);

        User user = userDatabase.get(userId);
        if (user != null) {
            user.setActive(false);
            userDatabase.put(userId, user);
            System.out.println("User deactivated: " + user.getEmail());
            return true;
        }

        System.out.println("Deactivation FAILED - User not found");
        return false;
    }

    // Debug method to see all users
    public void printAllUsers() {
        System.out.println("=== ALL USERS IN DATABASE ===");
        userDatabase.values().forEach(user -> {
            System.out.println("User: " + user.getEmail() + " | Password: " + user.getPassword() + " | Active: " + user.isActive());
        });
        System.out.println("Total: " + userDatabase.size() + " users");
    }
}