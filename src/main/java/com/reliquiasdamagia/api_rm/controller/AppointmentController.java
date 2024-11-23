package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AppointmentRequest;
import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.service.AppointmentService;
import com.reliquiasdamagia.api_rm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/tarot/appointments")
@RequiredArgsConstructor
public class AppointmentController {
    private final AppointmentService appointmentService;
    private final UserService userService;

    @PostMapping
    public ResponseEntity<Appointment> createAppointment(@RequestBody AppointmentRequest request) {

        Appointment appointment = appointmentService.createAppointment(
                request.getConsultantId(),
                request.getUserId(),
                request.getDateTime()
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(appointment);
    }

    @PutMapping("/{appointmentId}/status")
    public ResponseEntity<String> updateStatus(
            @PathVariable Long appointmentId,
            @RequestParam String status) {
        appointmentService.updateAppointmentStatus(appointmentId, status);
        return ResponseEntity.ok("Status atualizado com sucesso.");
    }

}
