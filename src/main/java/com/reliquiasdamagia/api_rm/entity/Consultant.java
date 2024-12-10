package com.reliquiasdamagia.api_rm.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "consultants")
public class Consultant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    @JsonManagedReference
    private User user;

    @Column(nullable = false)
    private String bio;

    @Column(nullable = false)
    private String specialties;

    @Column(nullable = false)
    private String imageUrl;

    @Column(nullable = false)
    private BigDecimal price;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Availability> availabilities;

    @OneToMany(mappedBy = "consultant", cascade = CascadeType.ALL)
    @JsonManagedReference
    private List<Appointment> appointments;
}
