package project.training.com.example.demo.security;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import project.training.com.example.demo.config.app.AppProperties;

public class JwtServiceTest {
    private static final String SECRET_KEY = "my-super-secret-key-my-super-secret-key-1234"; 
    private JwtService jwtService;
    private AppProperties appProperties;

    @BeforeEach
    void setUp() {
        appProperties = new AppProperties();
        appProperties.setSecretKey(SECRET_KEY);
        appProperties.setJwtExpiration(3600L);

        jwtService = new JwtService(appProperties);
    }

    @Test
    void shouldGenerateAndValidateToken() {
        String username = "testuser";

        String token = jwtService.generateToken(username);

        assertNotNull(token);

        String extracted = jwtService.extractUsername(token);
        assertEquals(username, extracted);

        boolean valid = jwtService.isValid(token, username);
        assertTrue(valid);
    }

    @Test
    void shouldFailValidationWhenUsernameMismatch() {
        String token = jwtService.generateToken("user1");

        boolean valid = jwtService.isValid(token, "user2");

        assertFalse(valid);
    }

    @Test
    void shouldThrowExceptionForInvalidToken() {
        String fakeToken = "invalid.token.value";

        assertThrows(Exception.class, () -> {
            jwtService.extractUsername(fakeToken);
        });
    }
}
