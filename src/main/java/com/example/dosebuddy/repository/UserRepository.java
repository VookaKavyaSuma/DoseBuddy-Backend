package com.example.dosebuddy.repository;

import com.example.dosebuddy.model.User;
import com.example.dosebuddy.model.UserRole;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Repository
public class UserRepository {
    private final Map<String, User> usersById = new ConcurrentHashMap<>();
    private final Map<String, String> emailToIdMap = new ConcurrentHashMap<>();

    public UserRepository(PasswordEncoder passwordEncoder) {
        // Pre-populate with default demo caregiver and patient users
        String caregiverPass = passwordEncoder.encode("password123");
        User caregiver = new User(
                "usr_caregiver_1",
                "caregiver@dosebuddy.com",
                "caregiver_sarah",
                "Sarah Jenkins (Caregiver)",
                caregiverPass,
                UserRole.CAREGIVER
        );
        save(caregiver);

        String patientPass = passwordEncoder.encode("password123");
        User patient = new User(
                "usr_patient_1",
                "patient@dosebuddy.com",
                "patient_robert",
                "Robert Jenkins (Senior)",
                patientPass,
                UserRole.PATIENT
        );
        save(patient);
    }

    public User save(User user) {
        if (user.getId() == null || user.getId().isEmpty()) {
            user.setId("usr_" + UUID.randomUUID().toString().replace("-", "").substring(0, 12));
        }
        usersById.put(user.getId(), user);
        if (user.getEmail() != null) {
            emailToIdMap.put(user.getEmail().toLowerCase().trim(), user.getId());
        }
        return user;
    }

    public Optional<User> findById(String id) {
        if (id == null) return Optional.empty();
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByEmail(String email) {
        if (email == null) return Optional.empty();
        String id = emailToIdMap.get(email.toLowerCase().trim());
        if (id == null) return Optional.empty();
        return Optional.ofNullable(usersById.get(id));
    }

    public Optional<User> findByEmailOrUsername(String identifier) {
        if (identifier == null) return Optional.empty();
        String search = identifier.toLowerCase().trim();
        return usersById.values().stream()
                .filter(u -> (u.getEmail() != null && u.getEmail().equalsIgnoreCase(search))
                        || (u.getUsername() != null && u.getUsername().equalsIgnoreCase(search)))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        if (email == null) return false;
        return emailToIdMap.containsKey(email.toLowerCase().trim());
    }
}
