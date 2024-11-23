package com.reliquiasdamagia.api_rm.dto;

import com.reliquiasdamagia.api_rm.entity.Order;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String name;
    private String email;
    private String phoneNumber;
    private String address;
    private String city;
    private String state;
    private String country;
    private LocalDateTime dateOfBirth;
    private List<Order> orders;
}
