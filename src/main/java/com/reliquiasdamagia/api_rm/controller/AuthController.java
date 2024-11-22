package com.reliquiasdamagia.api_rm.controller;

import com.reliquiasdamagia.api_rm.dto.AuthRequest;
import com.reliquiasdamagia.api_rm.dto.AuthResponse;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.service.AuthService;
import com.reliquiasdamagia.api_rm.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        if (authService.existsUser(user)) {
            return ResponseEntity.badRequest().body("E-mail já registrado.");
        }

        authService.registerUser(user);

        return ResponseEntity.ok(user);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthRequest authRequest) {
        if (userService.getUserByEmail(authRequest.getEmail()).isEmpty()) {
            return ResponseEntity.badRequest().body("Usuário não existe!");
        }

        AuthResponse authResponse = authService.loginUser(authRequest);

        return ResponseEntity.ok(authResponse);
    }

}
