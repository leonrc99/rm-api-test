package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.Appointment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
    List<Appointment> findByConsultantId(Long consultantId);
}
