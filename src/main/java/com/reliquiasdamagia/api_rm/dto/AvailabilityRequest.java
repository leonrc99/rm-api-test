package com.reliquiasdamagia.api_rm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AvailabilityRequest {
    private Long consultantId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Integer intervalMinutes = 1;
}
