package com.example.dosebuddy.config;

import com.example.dosebuddy.model.Medication;
import com.example.dosebuddy.model.User;
import com.example.dosebuddy.model.UserRole;
import com.example.dosebuddy.repository.MedicationRepository;
import com.example.dosebuddy.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final MedicationRepository medicationRepository;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepository,
                           MedicationRepository medicationRepository,
                           PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.medicationRepository = medicationRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        // 1. Seed Demo Caregiver
        if (!userRepository.existsByEmail("caregiver@dosebuddy.com")) {
            User caregiver = new User(
                    "usr_caregiver_1",
                    "caregiver@dosebuddy.com",
                    "caregiver_sarah",
                    "Sarah Jenkins (Caregiver)",
                    passwordEncoder.encode("password123"),
                    UserRole.CAREGIVER
            );
            userRepository.save(caregiver);
        }

        // 2. Seed Demo Patient
        if (!userRepository.existsByEmail("patient@dosebuddy.com")) {
            User patient = new User(
                    "usr_patient_1",
                    "patient@dosebuddy.com",
                    "patient_robert",
                    "Robert Jenkins (Senior)",
                    passwordEncoder.encode("password123"),
                    UserRole.PATIENT
            );
            userRepository.save(patient);
        }

        // 3. Seed Initial Demo Medications if table is empty
        if (medicationRepository.count() == 0) {
            medicationRepository.save(new Medication(
                    "Amlodipine Besylate",
                    "5mg - 1 Tablet Daily",
                    "For Blood Pressure Control",
                    "💊",
                    "current",
                    18,
                    30,
                    "Today at 8:00 PM",
                    "Take with full glass of water after dinner.",
                    "usr_caregiver_1"
            ));

            medicationRepository.save(new Medication(
                    "Metformin HC1",
                    "500mg - 2 Tablets Daily",
                    "For Blood Sugar Regulation",
                    "💊",
                    "current",
                    4, // Low inventory warning
                    60,
                    "Tomorrow at 8:00 AM",
                    "Take with breakfast.",
                    "usr_caregiver_1"
            ));

            medicationRepository.save(new Medication(
                    "Amoxicillin Antibiotic",
                    "250mg - 3 Times Daily",
                    "For Throat Infection",
                    "💊",
                    "past",
                    0,
                    10,
                    "Course Completed",
                    "Full 10-day prescription finished.",
                    "usr_caregiver_1"
            ));
        }
    }
}
