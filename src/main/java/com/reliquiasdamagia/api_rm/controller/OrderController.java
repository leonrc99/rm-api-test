package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<?> createOrder(@RequestParam Long userId) {
        try {
            Order order = orderService.createOrder(userId);
            return ResponseEntity.status(HttpStatus.CREATED).body(order);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao criar pedido: " + ex.getMessage());
        }
    }

    @GetMapping
    public ResponseEntity<?> getOrdersByUser(@RequestParam Long userId) {
        try {
            List<Order> orders = orderService.getOrdersByUser(userId);
            return ResponseEntity.ok(orders);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar pedidos: " + ex.getMessage());
        }
    }
}
