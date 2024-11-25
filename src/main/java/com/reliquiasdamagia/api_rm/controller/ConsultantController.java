package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.Consultant;
import com.reliquiasdamagia.api_rm.service.AppointmentService;
import com.reliquiasdamagia.api_rm.service.ConsultantService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tarot/consultants")
@RequiredArgsConstructor
public class ConsultantController {
    private final ConsultantService consultantService;
    private final AppointmentService appointmentService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> registerConsultant(
            @PathVariable Long userId,
            @RequestParam String bio,
            @RequestParam String specialties) {
        try {
            Consultant consultant = consultantService.registerConsultant(userId, bio, specialties);
            return ResponseEntity.status(HttpStatus.CREATED).body(consultant);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao registrar consultor: " + ex.getMessage());
        }
    }

    @GetMapping("/{consultantId}/appointments")
    public ResponseEntity<?> getAppointments(@PathVariable Long consultantId) {
        try {
            List<Appointment> appointments = appointmentService.getAppointmentsByConsultant(consultantId);
            return ResponseEntity.ok(appointments);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Consultor não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar compromissos: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getConsultants() {
        try {
            List<Consultant> consultants = consultantService.getAllConsultants();
            return ResponseEntity.ok(consultants);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar consultores: " + ex.getMessage());
        }
    }
}
