package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.Consultant;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.repository.ConsultantRepository;
import com.reliquiasdamagia.api_rm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ConsultantService {
    private final ConsultantRepository consultantRepository;
    private final UserRepository userRepository;
    private final AppointmentService appointmentService;

    public Consultant registerConsultant(Long userId, String bio, String specialties) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.isConsultant()) {
            throw new RuntimeException("User is not a consultant");
        }

        Consultant consultant = new Consultant();
        consultant.setUser(user);
        consultant.setBio(bio);
        consultant.setSpecialties(specialties);

        return consultantRepository.save(consultant);
    }

    public List<Consultant> getAllConsultants() {
        return consultantRepository.findAll();
    }
}
