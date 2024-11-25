package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AuthRequest;
import com.reliquiasdamagia.api_rm.dto.AuthResponse;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.service.AuthService;
import com.reliquiasdamagia.api_rm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        try {
            if (authService.existsUser(user)) {
                return ResponseEntity.badRequest().body("E-mail já registrado.");
            }
            authService.registerUser(user);
            return ResponseEntity.ok(user);
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao registrar usuário: " + ex.getMessage());
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        try {
            if (userService.getUserByEmail(authRequest.getEmail()).isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuário não encontrado.");
            }
            AuthResponse authResponse = authService.loginUser(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (AuthenticationException ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciais inválidas.");
        } catch (Exception ex) {
            return ResponseEntity.internalServerError().body("Erro ao fazer login: " + ex.getMessage());
        }
    }

}
