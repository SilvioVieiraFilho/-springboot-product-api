package com.produtoapi.security;
import com.produtoapi.security.filter.JwtAuthenticationFilter;
import com.produtoapi.security.service.JwtService;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

class JwtServiceTest {
    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;
    private final JwtService jwtService = new JwtService();

    @Nested
    class GenerateToken {

        @Test
        void deveGerarTokenComSucesso() {
            String token = jwtService.generateToken("teste@email.com", "USER");

            assertNotNull(token);
            assertFalse(token.isBlank());
        }
    }

    @Nested
    class ExtractUsername {

        @Test
        void deveExtrairUsernameComSucesso() {
            String token = jwtService.generateToken("teste@email.com", "USER");

            String username = jwtService.extractUsername(token);

            assertEquals("teste@email.com", username);
        }
    }

    @Nested
    class ExtractRole {

        @Test
        void deveExtrairRoleComSucesso() {
            String token = jwtService.generateToken("teste@email.com", "ADMIN");

            String role = jwtService.extractRole(token);

            assertEquals("ADMIN", role);
        }
    }

    @Nested
    class IsTokenValid {

        @Test
        void deveValidarTokenComSucesso() {
            String email = "teste@email.com";
            String token = jwtService.generateToken(email, "USER");

            boolean result = jwtService.isTokenValid(token, email);

            assertTrue(result);
        }

        @Test
        void deveInvalidarTokenComEmailDiferente() {
            String token = jwtService.generateToken("teste@email.com", "USER");

            boolean result = jwtService.isTokenValid(token, "outro@email.com");

            assertFalse(result);
        }
    }


    }
