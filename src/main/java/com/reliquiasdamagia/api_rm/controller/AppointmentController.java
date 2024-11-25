package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AppointmentRequest;
import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.enums.AppointmentStatus;
import com.reliquiasdamagia.api_rm.service.AppointmentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tarot/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request) {
        try {
            Appointment appointment = appointmentService.createAppointment(
                    request.getConsultantId(),
                    request.getUserId(),
                    request.getDateTime()
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Dados inválidos: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar compromisso: " + ex.getMessage());
        }
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<?> updateStatus(
            @PathVariable Long appointmentId,
            @RequestParam AppointmentStatus status) {
        try {
            appointmentService.updateAppointmentStatus(appointmentId, status);
            return ResponseEntity.ok("Status atualizado com sucesso.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compromisso não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar status: " + ex.getMessage());
        }
    }

}
