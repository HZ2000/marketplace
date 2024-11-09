package com.haykz.service;

import com.haykz.dto.CreateUserDto;
import com.haykz.dto.UserDto;
import com.haykz.dto.authentication.AuthRequestDto;
import com.haykz.dto.authentication.AuthResponseDto;
import com.haykz.entity.UserEntity;

/**
 * Interface for handling authentication-related operations in the application.
 * <p>
 * This service defines methods for authenticating users, registering users, and retrieving user information.
 * The methods are used for logging in, creating a new user, and managing user sessions.
 * </p>
 */
public interface AuthService {
    AuthResponseDto authenticateUser(AuthRequestDto authRequest);
    UserEntity findUserByUsername(String username);
    UserDto registerUser(CreateUserDto userEntity);
}
