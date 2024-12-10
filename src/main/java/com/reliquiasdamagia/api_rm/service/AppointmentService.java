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
    private final PaymentProcessor paymentProcessor;

    public Appointment createAppointment(Long consultantId, Long userId, LocalDateTime dateTime, Integer durationInMinutes) {
        // Validar se o consultor existe
        Consultant consultant = consultantRepository.findById(consultantId)
                .orElseThrow(() -> new RuntimeException("Consultor não encontrado."));

        // Validar se o usuário existe
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado."));

        // Validar disponibilidade para todos os minutos do intervalo
        for (int i = 0; i < durationInMinutes; i++) {
            LocalDateTime slot = dateTime.plusMinutes(i);
            Availability availability = availabilityRepository.findByConsultantIdAndDateTime(consultantId, slot)
                    .orElseThrow(() -> new RuntimeException("Horário " + slot + " não está disponível."));
            if (!availability.isAvailable()) {
                throw new RuntimeException("Horário " + slot + " já foi reservado.");
            }
            // Atualizar disponibilidade
            availability.setAvailable(false);
            availabilityRepository.save(availability);
        }

        // Criar o compromisso
        Appointment appointment = new Appointment();
        appointment.setConsultant(consultant);
        appointment.setUser(user);
        appointment.setDateTime(dateTime);
        appointment.setDurationInMinutes(durationInMinutes);
        appointment.setStatus(AppointmentStatus.PENDING);

        Appointment savedAppointment = appointmentRepository.save(appointment);

        // Gerar pagamento automaticamente
        try {
            String paymentLink = paymentProcessor.processAppointmentPayment(appointment, user.getEmail());
            // Salvar link de pagamento no compromisso
            savedAppointment.setPaymentId(paymentLink);
            appointmentRepository.save(savedAppointment);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao criar pagamento: " + e.getMessage());
        }

        return savedAppointment;
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
