package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import com.reliquiasdamagia.api_rm.entity.enums.CartStatus;
import com.reliquiasdamagia.api_rm.service.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/payments")
public class PaymentController {
    private final ShoppingCartService shoppingCartService;
    private final PaymentService paymentService;
    private final OrderService orderService;
    private final UserService userService;
    private final AppointmentService appointmentService;

    @PostMapping("/{userId}")
    public ResponseEntity<?> createPayment(@PathVariable Long userId) {
        try {
            ShoppingCart shoppingCart = shoppingCartService.getCart(userId);
            if (!shoppingCart.getStatus().equals(CartStatus.DRAFT)) {
                return ResponseEntity.badRequest().body("Apenas carrinhos com status DRAFT podem ser pagos.");
            }

            Order order = orderService.createOrderFromCart(shoppingCart);
            String payerEmail = userService.getUserEmailById(userId);
            String paymentLink = paymentService.createPaymentFromOrder(order, payerEmail);

            return ResponseEntity.ok(paymentLink);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho ou usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar pagamento: " + ex.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<?> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            String eventType = (String) payload.get("type");
            Long paymentId = Long.valueOf((Integer) payload.get("id"));

            if ("payment".equals(eventType)) {
                boolean isUpdated = paymentService.processPaymentNotification(paymentId);
                if (isUpdated) {
                    return ResponseEntity.ok("Webhook processado com sucesso.");
                }
            }
            return ResponseEntity.badRequest().body("Evento não suportado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao processar webhook: " + ex.getMessage());
        }
    }

    @PostMapping("/appointments/{appointmentId}")
    public ResponseEntity<?> createPaymentForAppointment(@PathVariable Long appointmentId) {
        try {
            String payerEmail = appointmentService.getUserEmailByAppointment(appointmentId);
            String paymentLink = paymentService.createPaymentForAppointment(appointmentId, payerEmail);
            return ResponseEntity.ok(paymentLink);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Compromisso não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar pagamento: " + ex.getMessage());
        }
    }
}
