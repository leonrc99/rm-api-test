package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.Consultant;
import com.reliquiasdamagia.api_rm.service.AppointmentService;
import com.reliquiasdamagia.api_rm.service.ConsultantService;
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
    public ResponseEntity<Consultant> registerConsultant(
            @PathVariable Long userId,
            @RequestParam String bio,
            @RequestParam String specialties) {
        Consultant consultant = consultantService.registerConsultant(userId, bio, specialties);
        return ResponseEntity.status(HttpStatus.CREATED).body(consultant);
    }

    @GetMapping("/{consultantId}/appointments")
    public ResponseEntity<List<Appointment>> getAppointments(@PathVariable Long consultantId) {
        List<Appointment> appointments = appointmentService.getAppointmentsByConsultant(consultantId);
        return ResponseEntity.ok(appointments);
    }

    @GetMapping
    public ResponseEntity<List<Consultant>> getConsultants() {
        List<Consultant> consultants = consultantService.getAllConsultants();
        return ResponseEntity.ok(consultants);
    }
}
