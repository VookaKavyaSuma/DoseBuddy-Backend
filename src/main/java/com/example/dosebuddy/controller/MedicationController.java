package com.example.dosebuddy.controller;

import com.example.dosebuddy.model.Medication;
import com.example.dosebuddy.repository.MedicationRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/medications")
@CrossOrigin(origins = {"http://localhost:5173", "http://127.0.0.1:5173", "http://localhost:3000"})
public class MedicationController {

    private final MedicationRepository medicationRepository;

    public MedicationController(MedicationRepository medicationRepository) {
        this.medicationRepository = medicationRepository;
    }

    @GetMapping
    public ResponseEntity<List<Medication>> getAllMedications(@RequestParam(required = false) String category) {
        if (category != null && !category.isBlank()) {
            return ResponseEntity.ok(medicationRepository.findByCategory(category));
        }
        return ResponseEntity.ok(medicationRepository.findAllByOrderByCreatedAtDesc());
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getMedicationById(@PathVariable Long id) {
        return medicationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<?> createMedication(@RequestBody Medication medication) {
        try {
            if (medication.getName() == null || medication.getName().isBlank()) {
                return ResponseEntity.badRequest().body(Map.of("error", "Medicine name is required."));
            }
            if (medication.getPhoto() == null || medication.getPhoto().isBlank()) {
                medication.setPhoto("💊");
            }
            if (medication.getCategory() == null || medication.getCategory().isBlank()) {
                medication.setCategory("current");
            }
            if (medication.getRemainingDoses() == null) {
                medication.setRemainingDoses(medication.getTotalDays() != null ? medication.getTotalDays() : 30);
            }
            if (medication.getTotalDays() == null) {
                medication.setTotalDays(30);
            }

            Medication saved = medicationRepository.save(medication);
            return ResponseEntity.status(HttpStatus.CREATED).body(saved);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to save medication: " + e.getMessage()));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateMedication(@PathVariable Long id, @RequestBody Medication details) {
        return medicationRepository.findById(id)
                .map(med -> {
                    if (details.getName() != null) med.setName(details.getName());
                    if (details.getDosage() != null) med.setDosage(details.getDosage());
                    if (details.getReason() != null) med.setReason(details.getReason());
                    if (details.getCategory() != null) med.setCategory(details.getCategory());
                    if (details.getRemainingDoses() != null) med.setRemainingDoses(details.getRemainingDoses());
                    if (details.getTotalDays() != null) med.setTotalDays(details.getTotalDays());
                    if (details.getNextDose() != null) med.setNextDose(details.getNextDose());
                    if (details.getCaregiverNote() != null) med.setCaregiverNote(details.getCaregiverNote());
                    Medication updated = medicationRepository.save(med);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/take")
    public ResponseEntity<?> takeDose(@PathVariable Long id) {
        return medicationRepository.findById(id)
                .map(med -> {
                    if (med.getRemainingDoses() != null && med.getRemainingDoses() > 0) {
                        med.setRemainingDoses(med.getRemainingDoses() - 1);
                        if (med.getRemainingDoses() == 0) {
                            med.setCategory("past");
                            med.setNextDose("Course Completed");
                        }
                    }
                    Medication updated = medicationRepository.save(med);
                    return ResponseEntity.ok(updated);
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteMedication(@PathVariable Long id) {
        if (!medicationRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        medicationRepository.deleteById(id);
        return ResponseEntity.ok(Map.of("message", "Medication deleted successfully"));
    }
}
