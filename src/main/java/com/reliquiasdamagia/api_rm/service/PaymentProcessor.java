package com.reliquiasdamagia.api_rm.service;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.reliquiasdamagia.api_rm.entity.Appointment;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PaymentProcessor {
    private final PaymentService paymentService;

    public String processAppointmentPayment(Appointment appointment, String payerEmail) throws MPException, MPApiException {
        return paymentService.createPaymentForAppointment(appointment.getId(), payerEmail);
    }
}