package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.entity.Order;
import com.reliquiasdamagia.api_rm.entity.Product;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.security.JwtUtil;
import com.reliquiasdamagia.api_rm.service.UserService;
import jakarta.persistence.EntityNotFoundException;
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
    public ResponseEntity<?> getAllUsers() {
        try {
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar usuários: " + ex.getMessage());
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getUserProfile(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
            return ResponseEntity.ok(user);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Perfil do usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar perfil: " + ex.getMessage());
        }
    }

    @PutMapping("/me")
    public ResponseEntity<?> updateUserProfile(
            @RequestHeader("Authorization") String token,
            @RequestBody User updatedUser) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
            User updated = userService.updateUserProfile(user.getId(), updatedUser);
            return ResponseEntity.ok(updated);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao atualizar perfil: " + ex.getMessage());
        }
    }

    @PostMapping("/me/favorites/{productId}")
    public ResponseEntity<?> addFavorite(@RequestHeader("Authorization") String token, @PathVariable Long productId) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
            userService.addProductToFavorites(user.getId(), productId);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou produto não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao adicionar favorito: " + ex.getMessage());
        }
    }

    @DeleteMapping("/me/favorites/{productId}")
    public ResponseEntity<?> removeFavorite(@RequestHeader("Authorization") String token, @PathVariable Long productId) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
            userService.removeProductFromFavorites(user.getId(), productId);
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou produto não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao remover favorito: " + ex.getMessage());
        }
    }

    @GetMapping("/me/favorites")
    public ResponseEntity<?> getFavorites(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
            List<Product> favorites = userService.getUserFavorites(user.getId());
            return ResponseEntity.ok(favorites);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar favoritos: " + ex.getMessage());
        }
    }

    @GetMapping("/me/orders")
    public ResponseEntity<?> getOrderHistory(@RequestHeader("Authorization") String token) {
        try {
            String email = jwtUtil.extractUsername(token.substring(7));
            User user = userService.getUserByEmail(email)
                    .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado."));
            List<Order> orders = userService.getUserOrderHistory(user.getId());
            return ResponseEntity.ok(orders);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Histórico de pedidos não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao buscar histórico de pedidos: " + ex.getMessage());
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteById(@PathVariable Long id) {
        try {
            userService.deleteUserById(id);
            return ResponseEntity.ok("Usuário excluido com sucesso");
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário ou produto não encontrado.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao remover favorito: " + ex.getMessage());
        }
    }
}
