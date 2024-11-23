package com.reliquiasdamagia.api_rm.controller;

import com.mercadopago.exceptions.MPApiException;
import com.mercadopago.exceptions.MPException;
import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.ShoppingCart;
import com.reliquiasdamagia.api_rm.entity.enums.CartStatus;
import com.reliquiasdamagia.api_rm.service.*;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<String> createPayment(@PathVariable Long userId) {
        try {
            // Recupera o carrinho
            ShoppingCart shoppingCart = shoppingCartService.getCart(userId);

            // Validar status do carrinho
            if (!shoppingCart.getStatus().equals(CartStatus.DRAFT)) {
                return ResponseEntity.badRequest().body("Apenas carrinhos com status DRAFT podem ser pagos.");
            }

            // Criar a ordem no banco de dados
            Order order = orderService.createOrderFromCart(shoppingCart);

            // E-mail do usuário (exemplo: autenticado)
            String payerEmail = userService.getUserEmailById(userId);

            // Criar a preferência de pagamento
            String paymentLink = paymentService.createPaymentFromOrder(order, payerEmail);

            return ResponseEntity.ok(paymentLink);
        } catch (MPException | MPApiException e) {
            return ResponseEntity.internalServerError().body("Erro ao criar pagamento: " + e.getMessage());
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(@RequestBody Map<String, Object> payload) {
        try {
            // Extrair informações úteis do payload
            String eventType = (String) payload.get("type");
            Long paymentId = (Long) payload.get("id");

            // Processar notificações específicas (ex.: pagamento aprovado)
            if ("payment".equals(eventType)) {
                // Aqui, busque o pagamento no MercadoPago e atualize o pedido
                // Este metodo deve ser implementado no serviço
                boolean isUpdated = paymentService.processPaymentNotification(paymentId);

                if (isUpdated) {
                    return ResponseEntity.ok("Webhook processado com sucesso.");
                }
            }

            return ResponseEntity.badRequest().body("Evento não suportado.");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro ao processar webhook: " + e.getMessage());
        }
    }

    @PostMapping("/appointments/{appointmentId}")
    public ResponseEntity<String> createPaymentForAppointment(
            @PathVariable Long appointmentId) {
        try {
            String payerEmail = appointmentService.getUserEmailByAppointment(appointmentId);
            String paymentLink = paymentService.createPaymentForAppointment(appointmentId, payerEmail);

            return ResponseEntity.ok(paymentLink);
        } catch (MPException | MPApiException e) {
            return ResponseEntity.internalServerError().body("Erro ao criar pagamento: " + e.getMessage());
        }
    }
}
