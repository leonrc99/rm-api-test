package com.reliquiasdamagia.api_rm.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        // Auth
                        .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()

                        // Appointment
                        .requestMatchers(HttpMethod.POST, "/api/tarot/appointments").hasAnyAuthority("USER", "CONSULTANT")
                        .requestMatchers(HttpMethod.PUT, "/api/tarot/appointments/**").hasAnyAuthority("CONSULTANT", "ADMIN")

                        // Availability
                        .requestMatchers(HttpMethod.POST, "/api/tarot/consultant/availability/**").hasAuthority("CONSULTANT")

                        // Category
                        .requestMatchers(HttpMethod.GET, "/api/category/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/category/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/category/**").hasAuthority("ADMIN")

                        // Consultant
                        .requestMatchers(HttpMethod.POST, "/api/tarot/consultants/**").hasAuthority("CONSULTANT")
                        .requestMatchers(HttpMethod.GET, "/api/tarot/consultants/**").permitAll()

                        // Order
                        .requestMatchers(HttpMethod.POST, "/api/orders").authenticated()
                        .requestMatchers(HttpMethod.GET, "/api/orders").authenticated()

                        // Payments
                        .requestMatchers(HttpMethod.POST, "/api/payments/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/payments/webhook").permitAll()

                        // Products
                        .requestMatchers(HttpMethod.GET, "/api/product/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/product/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/product/**").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/product/**").hasAuthority("ADMIN")

                        // ShoppingCart
                        .requestMatchers(HttpMethod.GET, "/api/cart/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/cart/items").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/api/cart/**").authenticated()

                        // Users
                        .requestMatchers(HttpMethod.GET, "/api/users").hasAuthority("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/users/me/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/users/me/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}


