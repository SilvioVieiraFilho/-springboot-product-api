package com.produtoapi.security;
import com.produtoapi.security.service.JwtService;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


class JwtServiceTest {

    private final JwtService jwtService = new JwtService();

    @Test
    void deveGerarTokenComSucesso() {
        String token = jwtService.generateToken("user@email.com", "USER");

        assertNotNull(token);
        assertFalse(token.isEmpty());
    }


    @Test
    void deveExtrairUsernameDoToken() {
        String token = jwtService.generateToken("user@email.com", "USER");

        String username = jwtService.extractUsername(token);

        assertEquals("user@email.com", username);
    }


    @Test
    void deveExtrairRoleDoToken() {
        String token = jwtService.generateToken("user@email.com", "ADMIN");

        String role = jwtService.extractRole(token);

        assertEquals("ADMIN", role);
    }


    @Test
    void deveValidarTokenCorretamente() {
        String email = "user@email.com";
        String token = jwtService.generateToken(email, "USER");

        boolean valid = jwtService.isTokenValid(token, email);

        assertTrue(valid);
    }

    @Test
    void deveInvalidarTokenComEmailDiferente() {
        String token = jwtService.generateToken("user@email.com", "USER");

        boolean valid = jwtService.isTokenValid(token, "outro@email.com");

        assertFalse(valid);
    }

    @Test
    void deveFalharComTokenInvalido() {
        assertThrows(Exception.class, () -> {
            jwtService.extractUsername("token-invalido");
        });
    }
}