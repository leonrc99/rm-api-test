package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.*;
import com.reliquiasdamagia.api_rm.service.ShoppingCartService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    @GetMapping("{userId}")
    public ResponseEntity<?> getCart(@PathVariable Long userId) {
        try {
            ShoppingCart cart = cartService.getCart(userId);
            return ResponseEntity.ok(cart);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho não encontrado para o usuário especificado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar carrinho: " + ex.getMessage());
        }
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {
        try {
            cartService.addItemToCart(userId, productId, quantity);
            return ResponseEntity.ok("Produto adicionado ao carrinho com sucesso.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou produto não encontrado.");
        } catch (IllegalArgumentException ex) {
            return ResponseEntity.badRequest().body("Quantidade inválida: " + ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao adicionar item ao carrinho: " + ex.getMessage());
        }
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItemFromCart(
            @RequestParam Long userId,
            @PathVariable Long itemId) {
        try {
            cartService.removeItemFromCart(userId, itemId);
            return ResponseEntity.ok("Produto removido do carrinho com sucesso.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Item ou carrinho não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao remover item do carrinho: " + ex.getMessage());
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam Long userId) {
        try {
            cartService.clearCart(userId);
            return ResponseEntity.ok("Carrinho limpo com sucesso.");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Carrinho não encontrado para o usuário especificado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao limpar carrinho: " + ex.getMessage());
        }
    }

}
