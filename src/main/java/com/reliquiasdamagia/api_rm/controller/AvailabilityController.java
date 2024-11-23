package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AvailabilityRequest;
import com.reliquiasdamagia.api_rm.entity.Availability;
import com.reliquiasdamagia.api_rm.service.AvailabilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/tarot/consultant/availability")
public class AvailabilityController {
    private final AvailabilityService availabilityService;

    @PostMapping("/interval")
    public ResponseEntity<List<Availability>> createAvailabilityInterval(
            @RequestBody AvailabilityRequest request
    ) {
        List<Availability> availabilities = availabilityService.createAvailabilityInterval(
                request.getConsultantId(),
                request.getStartTime(),
                request.getEndTime(),
                Duration.ofMinutes(request.getIntervalMinutes())
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(availabilities);
    }
}
