package com.reliquiasdamagia.api_rm.dto;

import lombok.Data;

@Data
public class AuthRequest {
    private String email;
    private String password;
}
