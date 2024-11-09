package com.haykz.service.impl;

import com.haykz.dto.CreateUserDto;
import com.haykz.dto.UserDto;
import com.haykz.dto.authentication.AuthRequestDto;
import com.haykz.dto.authentication.AuthResponseDto;
import com.haykz.entity.UserEntity;
import com.haykz.repository.UserRepository;
import com.haykz.security.JWTUtil;
import com.haykz.service.AuthService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Implementation of the {@link AuthService} interface that provides authentication and user registration functionality.
 * <p>
 * This service handles the authentication process by validating user credentials, generating JWT tokens,
 * and registering new users. It communicates with the {@link UserRepository} for data access and uses
 * {@link PasswordEncoder} to securely store passwords and {@link JWTUtil} for token management.
 * </p>
 */
@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;

    @Override
    public AuthResponseDto authenticateUser(AuthRequestDto authRequest) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
            );
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException("Incorrect username or password", e);
        }

        UserEntity userDetails = findUserByUsername(authRequest.getUsername());
        String jwt = jwtUtil.generateToken(userDetails.getUsername());

        return new AuthResponseDto(jwt);
    }

    @Override
    public UserEntity findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Override
    public UserDto registerUser(CreateUserDto createUserDTO) {
        if (userRepository.existsByUsername(createUserDTO.getUsername())) {
            throw new IllegalArgumentException("Username is already taken");
        }

        UserEntity user = modelMapper.map(createUserDTO, UserEntity.class);
        user.setPassword(passwordEncoder.encode(createUserDTO.getPassword()));

        UserEntity savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }
}
