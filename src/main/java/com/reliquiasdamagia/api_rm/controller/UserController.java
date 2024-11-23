package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.Product;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.security.JwtUtil;
import com.reliquiasdamagia.api_rm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users);
    }

    // Visualizar perfil
    @GetMapping("/me")
    public ResponseEntity<User> getUserProfile(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));  // removendo "Bearer " do token
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User não encontrada"));
        return ResponseEntity.ok(user);
    }

    // Atualizar perfil
    @PutMapping("/me")
    public ResponseEntity<User> updateUserProfile(@RequestHeader("Authorization") String token,
                                                  @RequestBody User updatedUser) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User não encontrada"));
        User updated = userService.updateUserProfile(user.getId(), updatedUser);
        return ResponseEntity.ok(updated);
    }

    // Adicionar favorito
    @PostMapping("/me/favorites/{productId}")
    public ResponseEntity<Void> addFavorite(@RequestHeader("Authorization") String token,
                                            @PathVariable Long productId) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User não encontrada"));
        userService.addProductToFavorites(user.getId(), productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // Remover favorito
    @DeleteMapping("/me/favorites/{productId}")
    public ResponseEntity<Void> removeFavorite(@RequestHeader("Authorization") String token,
                                               @PathVariable Long productId) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User não encontrada"));
        userService.removeProductFromFavorites(user.getId(), productId);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    // Obter favoritos
    @GetMapping("/me/favorites")
    public ResponseEntity<List<Product>> getFavorites(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User não encontrada"));
        List<Product> favorites = userService.getUserFavorites(user.getId());
        return ResponseEntity.ok(favorites);
    }

    // Histórico de compras
    @GetMapping("/me/orders")
    public ResponseEntity<List<Order>> getOrderHistory(@RequestHeader("Authorization") String token) {
        String email = jwtUtil.extractUsername(token.substring(7));
        User user = userService.getUserByEmail(email)
                .orElseThrow(() -> new RuntimeException("User não encontrada"));
        List<Order> orders = userService.getUserOrderHistory(user.getId());
        return ResponseEntity.ok(orders);
    }
}
