package com.reliquiasdamagia.api_rm.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ConsultantRequest {
    private String bio;
    private String specialties;
    private String imageUrl;
    private BigDecimal price;
}
