package com.haykz.controller;

import com.haykz.dto.CreateUserDto;
import com.haykz.dto.UserDto;
import com.haykz.dto.authentication.AuthRequestDto;
import com.haykz.dto.authentication.AuthResponseDto;
import com.haykz.service.AuthService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    @Test
    public void authenticateUser_ShouldReturnAuthResponseDto_WhenCredentialsAreValid() {
        AuthRequestDto authRequest = AuthRequestDto.builder()
                .username("testuser")
                .password("password")
                .build();
        AuthResponseDto authResponse = AuthResponseDto.builder()
                .token("mocked-jwt-token")
                .build();

        when(authService.authenticateUser(any(AuthRequestDto.class))).thenReturn(authResponse);

        ResponseEntity<?> response = authController.authenticate(authRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("mocked-jwt-token",((ResponseEntity<AuthResponseDto>) response).getBody().getToken());
    }
    @Test
    public void authenticateUser_ShouldReturnUnauthorized_WhenCredentialsAreInvalid() {
        AuthRequestDto authRequest = AuthRequestDto.builder()
                .username("testuser")
                .password("wrongpassword")
                .build();

        when(authService.authenticateUser(any(AuthRequestDto.class)))
                .thenThrow(new BadCredentialsException("Incorrect username or password"));

        ResponseEntity<?> response = authController.authenticate(authRequest);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    public void registerUser_ShouldReturnUserDto_WhenUsernameIsUnique() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("testuser")
                .password("password")
                .build();
        UserDto userDto = UserDto.builder().username("testuser").build();

        when(authService.registerUser(any(CreateUserDto.class))).thenReturn(userDto);

        ResponseEntity<?> response = authController.registerUser(createUserDto);

        assertNotNull(response);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser",((ResponseEntity<UserDto>) response).getBody().getUsername());
    }

    @Test
    public void registerUser_ShouldReturnBadRequest_WhenUsernameIsAlreadyTaken() {
        CreateUserDto createUserDto = CreateUserDto.builder()
                .username("existinguser")
                .password("password")
                .build();

        when(authService.registerUser(any(CreateUserDto.class)))
                .thenThrow(new IllegalArgumentException("Username already taken"));

        ResponseEntity<?> response = authController.registerUser(createUserDto);

        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }
}


