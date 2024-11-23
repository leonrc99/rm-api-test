package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.Availability;
import com.reliquiasdamagia.api_rm.entity.Consultant;
import com.reliquiasdamagia.api_rm.repository.AvailabilityRepository;
import com.reliquiasdamagia.api_rm.repository.ConsultantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AvailabilityService {

    private final AvailabilityRepository availabilityRepository;
    private final ConsultantRepository consultantRepository;

    public List<Availability> createAvailabilityInterval(Long consultantId, LocalDateTime startTime, LocalDateTime endTime, Duration interval) {
        if (startTime.isAfter(endTime)) {
            throw new RuntimeException("O horário inicial não pode ser após o horário final.");
        }

        List<Availability> availabilities = new ArrayList<>();
        LocalDateTime currentTime = startTime;

        while (!currentTime.isAfter(endTime)) {
            // Verifica se já existe disponibilidade para o horário
            Optional<Availability> existingAvailability = availabilityRepository.findByConsultantIdAndDateTime(consultantId, currentTime);

            Consultant consultant = consultantRepository.findById(consultantId)
                    .orElseThrow(() -> new RuntimeException("Consultor não encontrada."));

            if (existingAvailability.isEmpty()) {
                // Cria nova disponibilidade
                Availability availability = new Availability();
                availability.setConsultant(consultant);
                availability.setDateTime(currentTime);
                availability.setAvailable(true);

                availabilities.add(availabilityRepository.save(availability));
            }

            currentTime = currentTime.plus(interval); // Incrementa o intervalo
        }

        return availabilities;
    }
}
