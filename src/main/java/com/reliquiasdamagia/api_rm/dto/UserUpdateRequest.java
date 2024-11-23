package com.reliquiasdamagia.api_rm.dto;

import lombok.Data;

@Data
public class UserUpdateRequest {
    private String name;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
}
