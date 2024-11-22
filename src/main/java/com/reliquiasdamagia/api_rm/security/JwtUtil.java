package com.reliquiasdamagia.api_rm.security;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;


@Component
public class JwtUtil {
    private final String SECRET_KEY = "Qa1whnOx5n/SLZgpXAZYvMGDqWNbQirPrp1smxB76Zc=";

    public String generateToken(String username, String name, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("name", name);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String extractUsername(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody().getSubject();
    }

    // Extrai o nome do usuário do token
    public String extractName(String token) {
        return (String) getClaimsFromToken(token).get("name");
    }

    // Extrai a role do usuário do token
    public String extractRole(String token) {
        return (String) getClaimsFromToken(token).get("role");
    }

    // Valida o token
    public boolean validateToken(String token) {
        try {
            getClaimsFromToken(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }

    // Obtem os claims do token
    private Claims getClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
    }
}
