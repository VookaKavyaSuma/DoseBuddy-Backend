package com.example.dosebuddy.model;

import java.time.LocalDateTime;

public class User {
    private String id;
    private String email;
    private String username;
    private String fullName;
    private String password; // Encrypted
    private UserRole role;
    private LocalDateTime createdAt;

    public User() {
    }

    public User(String id, String email, String username, String fullName, String password, UserRole role) {
        this.id = id;
        this.email = email;
        this.username = username;
        this.fullName = fullName;
        this.password = password;
        this.role = role;
        this.createdAt = LocalDateTime.now();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public UserRole getRole() {
        return role;
    }

    public void setRole(UserRole role) {
        this.role = role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
