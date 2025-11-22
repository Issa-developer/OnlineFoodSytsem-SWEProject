package com.foodapp.model;

import java.time.LocalDateTime;

public class User {
    private String userId;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String address;
    private LocalDateTime createdAt;
    private LocalDateTime lastLoginAt;
    private boolean active = true;

    public User() {
        this.createdAt = LocalDateTime.now();
    }

    public User(String username, String email, String password, String phoneNumber, String address) {
        this();
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.address = address;
    }

    // Getters and Setters
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public LocalDateTime getLastLoginAt() { return lastLoginAt; }
    public void setLastLoginAt(LocalDateTime lastLoginAt) { this.lastLoginAt = lastLoginAt; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
}