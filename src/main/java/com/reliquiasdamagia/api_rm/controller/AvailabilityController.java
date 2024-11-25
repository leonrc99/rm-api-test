package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AvailabilityRequest;
import com.reliquiasdamagia.api_rm.entity.Availability;
import com.reliquiasdamagia.api_rm.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tarot/consultant/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @PostMapping("/interval")
    public ResponseEntity<?> createAvailabilityInterval(@RequestBody AvailabilityRequest request) {
        try {
            List<Availability> availabilities = availabilityService.createAvailabilityInterval(
                    request.getConsultantId(),
                    request.getStartTime(),
                    request.getEndTime(),
                    Duration.ofMinutes(request.getIntervalMinutes())
            );
            return ResponseEntity.status(HttpStatus.CREATED).body(availabilities);
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Dados inválidos: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar intervalo de disponibilidade: " + ex.getMessage());
        }
    }
}
