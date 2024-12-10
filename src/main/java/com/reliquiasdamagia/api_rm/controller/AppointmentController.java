package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AppointmentRequest;
import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.enums.AppointmentStatus;
import com.reliquiasdamagia.api_rm.service.AppointmentService;
import com.reliquiasdamagia.api_rm.service.PaymentService;
import com.reliquiasdamagia.api_rm.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/tarot/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserService userService;
    private final PaymentService paymentService;

    @PostMapping
    public ResponseEntity<?> createAppointment(@RequestBody AppointmentRequest request, @AuthenticationPrincipal UserDetails userDetails) {
        try {
            Long userId = userService.getUserIdByEmail(userDetails.getUsername());

            if (userId == null) {
                return ResponseEntity.badRequest().body("Usuário não encontrado.");
            }

            Appointment appointment = appointmentService.createAppointment(
                    request.getConsultantId(),
                    userId,
                    request.getDateTime(),
                    request.getDurationInMinutes()
            );

            // Retorna o link de pagamento gerado
            return ResponseEntity.status(HttpStatus.CREATED).body(appointment.getPaymentId());
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
