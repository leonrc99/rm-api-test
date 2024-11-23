package com.reliquiasdamagia.api_rm.repository;

import com.reliquiasdamagia.api_rm.entity.Availability;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.Optional;

public interface AvailabilityRepository extends JpaRepository<Availability, Long> {
    Optional<Availability> findByConsultantIdAndDateTime(Long consultantId, LocalDateTime dateTime);
}
