package com.haykz.security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class JWTUtilTest {

    @InjectMocks
    private JWTUtil jwtUtil;

    private String secretKeyValue = "jasda2n13n1nakljfoidsamlkaslkdjk1jlk23";

    @BeforeEach
    void setUp() throws NoSuchFieldException, IllegalAccessException {
        MockitoAnnotations.openMocks(this);

        Field secretKeyField = JWTUtil.class.getDeclaredField("secretKey");
        secretKeyField.setAccessible(true);
        secretKeyField.set(jwtUtil, secretKeyValue);
    }

    @Test
    void generateToken_ShouldReturnValidToken_WhenCalledWithUsername() {
        String username = "testuser";

        String token = jwtUtil.generateToken(username);

        assertNotNull(token);
        assertTrue(token.startsWith("eyJ"));
    }

    @Test
    void extractUsername_ShouldReturnUsername_WhenTokenIsValid() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        String extractedUsername = jwtUtil.extractUsername(token);

        assertEquals(username, extractedUsername);
    }

    @Test
    void validateToken_ShouldReturnTrue_WhenTokenIsValid() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        Boolean isValid = jwtUtil.validateToken(token, username);

        assertTrue(isValid);
    }

    @Test
    void validateToken_ShouldReturnFalse_WhenTokenIsInvalid() {
        String username = "testuser";
        String token = jwtUtil.generateToken(username);

        Boolean isValid = jwtUtil.validateToken(token, "wronguser");

        assertFalse(isValid);
    }

    @Test
    void extractUsername_ShouldThrowException_WhenTokenIsMalformed() {
        String malformedToken = "malformed.token";

        assertThrows(Exception.class, () -> {
            jwtUtil.extractUsername(malformedToken);
        });
    }
}
