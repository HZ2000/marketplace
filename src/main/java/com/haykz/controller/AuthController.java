package com.haykz.controller;

import com.haykz.dto.CreateUserDto;
import com.haykz.dto.UserDto;
import com.haykz.dto.authentication.AuthRequestDto;
import com.haykz.dto.authentication.AuthResponseDto;
import com.haykz.service.AuthService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * The AuthController class is responsible for handling authentication and user registration.
 * It exposes two endpoints:
 * 1. /authenticate - for authenticating a user.
 * 2. /register - for registering a new user.
 * The controller delegates the logic of authentication and registration to the AuthService.
 */
@RequestMapping("/user")
@RestController
@AllArgsConstructor
public class AuthController {

    private final AuthService authService;

    /**
     * Authenticates a user based on the provided credentials.
     *
     * @param authRequest The authentication request containing the username and password
     * @return ResponseEntity containing either an AuthResponseDto with a JWT token or an error message
     */
    @PostMapping("/authenticate")
    public ResponseEntity<?> authenticate(@RequestBody AuthRequestDto authRequest) {
        try {
            AuthResponseDto authResponse = authService.authenticateUser(authRequest);
            return ResponseEntity.ok(authResponse);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    /**
     * Registers a new user in the system.
     *
     * @param createUserDTO The data transfer object containing the user registration details
     * @return ResponseEntity containing either a UserDto of the created user or an error message
     */
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@RequestBody CreateUserDto createUserDTO) {
        try {
            UserDto userDTO = authService.registerUser(createUserDTO);
            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}