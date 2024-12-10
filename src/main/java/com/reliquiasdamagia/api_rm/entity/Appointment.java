package com.reliquiasdamagia.api_rm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.reliquiasdamagia.api_rm.entity.enums.AppointmentStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "appointments")
public class Appointment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "consultant_id", nullable = false)
    @JsonBackReference
    private Consultant consultant;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime dateTime;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AppointmentStatus status; // PENDING, CONFIRMED, COMPLETED, CANCELED

    private String paymentId;

    @Column(nullable = false)
    private Integer durationInMinutes;
}