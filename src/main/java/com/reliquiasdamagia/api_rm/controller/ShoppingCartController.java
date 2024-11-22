package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.*;
import com.reliquiasdamagia.api_rm.service.ShoppingCartService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
public class ShoppingCartController {

    private final ShoppingCartService cartService;

    public ShoppingCartController(ShoppingCartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public ResponseEntity<ShoppingCart> getCart(@RequestParam Long userId) {
        ShoppingCart cart = cartService.getCart(userId);

        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<?> addItemToCart(@RequestParam Long userId, @RequestParam Long productId, @RequestParam int quantity) {
        cartService.addItemToCart(userId, productId, quantity);

        return ResponseEntity.ok("Produto adicionado ao carrinho.");
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<?> removeItemFromCart(@RequestParam Long userId, @PathVariable Long itemId) {
        cartService.removeItemFromCart(userId, itemId);

        return ResponseEntity.ok("Produto removido do carrinho.");
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(@RequestParam Long userId) {
        cartService.clearCart(userId);

        return ResponseEntity.ok("Carrinho limpo.");
    }


}
