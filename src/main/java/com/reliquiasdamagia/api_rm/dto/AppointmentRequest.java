package com.reliquiasdamagia.api_rm.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AppointmentRequest {
    private Long consultantId;
    private Long userId;
    private LocalDateTime dateTime;
}
