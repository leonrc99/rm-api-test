package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.entity.Appointment;
import com.reliquiasdamagia.api_rm.entity.Availability;
import com.reliquiasdamagia.api_rm.entity.Consultant;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.entity.enums.AppointmentStatus;
import com.reliquiasdamagia.api_rm.repository.AppointmentRepository;
import com.reliquiasdamagia.api_rm.repository.AvailabilityRepository;
import com.reliquiasdamagia.api_rm.repository.ConsultantRepository;
import com.reliquiasdamagia.api_rm.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository appointmentRepository;
    private final AvailabilityRepository availabilityRepository;
    private final ConsultantRepository consultantRepository;
    private final UserRepository userRepository;

    public Appointment createAppointment(Long consultantId, Long userId, LocalDateTime dateTime) {
        // Validar se o horário está disponível
        Availability availability = availabilityRepository.findByConsultantIdAndDateTime(consultantId, dateTime)
                .orElseThrow(() -> new RuntimeException("Horário não está disponível."));

        if (!availability.isAvailable()) {
            throw new RuntimeException("Este horário já foi reservado.");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        // Criar o agendamento
        Appointment appointment = new Appointment();
        appointment.setConsultant(availability.getConsultant());
        appointment.setUser(user);
        appointment.setDateTime(dateTime);
        appointment.setStatus(AppointmentStatus.PENDING);

        // Marcar o horário como indisponível
        availability.setAvailable(false);
        availabilityRepository.save(availability);

        return appointmentRepository.save(appointment);
    }

    public void updateAppointmentStatus(Long appointmentId, AppointmentStatus status) {
        Appointment appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada."));

        // Validar o novo status
        if (!List.of(AppointmentStatus.CONFIRMED, AppointmentStatus.COMPLETED, AppointmentStatus.CANCELED).contains(status)) {
            throw new RuntimeException("Status inválido.");
        }

        appointment.setStatus(status);
        appointmentRepository.save(appointment);
    }

    public List<Appointment> getAppointmentsByConsultant(Long consultantId) {
        return appointmentRepository.findByConsultantId(consultantId);
    }

    public String getUserEmailByAppointment(Long appointmentId) {
        return appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new RuntimeException("Consulta não encontrada."))
                .getUser()
                .getEmail();
    }
}
