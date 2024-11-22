package com.reliquiasdamagia.api_rm.service;

import com.reliquiasdamagia.api_rm.dto.AuthRequest;
import com.reliquiasdamagia.api_rm.dto.AuthResponse;
import com.reliquiasdamagia.api_rm.entity.User;
import com.reliquiasdamagia.api_rm.repository.UserRepository;
import com.reliquiasdamagia.api_rm.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public void registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public boolean existsUser(User user) {
        return userRepository.findByEmail(user.getEmail()).isPresent();
    }

    public AuthResponse loginUser(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));

        User user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        String token = jwtUtil.generateToken(user.getEmail(), user.getName(), user.getRole());

        return new AuthResponse(token);
    }
}
